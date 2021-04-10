package email.com.gmail.coolxiaom95.citytools.gui.hotkey;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.List;
import java.util.Set;

import org.lwjgl.input.Keyboard;

import net.minecraft.util.IntHashMap;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class MyKey implements Comparable<MyKey> {
	private static final List<MyKey> keybindArray = Lists.<MyKey>newArrayList();
	private static final IntHashMap<MyKey> hash = new IntHashMap<MyKey>();
	private static final Set<String> keybindSet = Sets.<String>newHashSet();
	private final String keyDescription;
	private int keyCode;
	private int pressTime;

	public static void onTick(int keyCode) {
		if (keyCode != 0) {
			MyKey keybinding = (MyKey) hash.lookup(keyCode);

			if (keybinding != null) {
				++keybinding.pressTime;
			}
		}
	}

	public static void resetKeyBindingArrayAndHash() {
		hash.clearMap();

		for (MyKey keybinding : keybindArray) {
			hash.addKey(keybinding.keyCode, keybinding);
		}
	}

	public static Set<String> getKeybinds() {
		return keybindSet;
	}

	public MyKey(String description, int keyCode) {
		this.keyDescription = description;
		this.keyCode = keyCode;
		keybindArray.add(this);
		hash.addKey(keyCode, this);
	}

	/**
	 * Returns true if the key is pressed (used for continuous querying). Should be
	 * used in tickers.
	 */
	public boolean isKeyDown() {
		return keyCode != 0 && Keyboard.isKeyDown(keyCode);
	}

	/**
	 * Returns true on the initial key press. For continuous querying use
	 * {@link isKeyDown()}. Should be used in key events.
	 */
	public boolean isPressed() {
		if (this.pressTime == 0) {
			return false;
		} else {
			--this.pressTime;
			return true;
		}
	}

	public String getKeyDescription() {
		return this.keyDescription;
	}

	public int getKeyCode() {
		return this.keyCode;
	}

	public void setKeyCode(int keyCode) {
		this.keyCode = keyCode;
	}

	public int compareTo(MyKey p_compareTo_1_) {
		return this.keyDescription.compareTo(p_compareTo_1_.keyDescription);
	}
}