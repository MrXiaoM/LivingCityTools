package email.com.gmail.coolxiaom95.citytools;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

import net.minecraft.launchwrapper.IClassTransformer;

public class ModTransformer implements IClassTransformer {

	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) {
		//if ("net.minecraft.client.gui.inventory.GuiInventory".equals(transformedName)) {
			
		//	ClassReader classReader = new ClassReader(basicClass);
			
		//	ClassWriter classWriter = new ClassWriter(1);
		//	classReader.accept(new ModTransformer.CV(classWriter), 8);
		//	System.out.println("物品栏页面注入完毕");
			//return classWriter.toByteArray();
		//}
		return basicClass;
	}
	
	class CV extends ClassVisitor {

		public CV(final ClassVisitor cv) {
			super(327680, cv);
		}

		@Override
		public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature,
				final String[] exceptions) {
			//System.out.println(name + " " + desc);
			if (name.equals("drawScreen") || (name.equals("a") && desc.equals("(IIF)V")) ) { // func_73866_w_
				//System.out.println("注入 "+name+" "+desc);
				final MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
				return new ModTransformer.CV.MV(327680, mv, (name.equals("a") && desc.equals("(IIF)V")));
			}
			//a(Lnet/minecraft/inventory/Slot;)V
			if (name.equals("drawSlot") || (name.equals("a") && desc.equals("(Lyg;)V"))) { // func_146284
				//System.out.println("注入 "+name+" "+desc);
				final MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
				return new ModTransformer.CV.MV1(327680, mv, (name.equals("a") && desc.equals("(Lyg;)V")));
			}
			return super.visitMethod(access, name, desc, signature, exceptions);
		}

		class MV extends MethodVisitor {

			Boolean mapping=false;
			public MV(int api, MethodVisitor methodVisitor,boolean mapping) {
				super(api, methodVisitor);
				final String tips = "禁止套娃!";
				tips.toString();
				this.mapping = mapping;
			}

			@Override
			public void visitCode() {
					mv.visitVarInsn(25, 0); // ALOAD 0
					// GETFIELD List GuiMerchant.buttonList
					mv.visitFieldInsn(180, "net/minecraft/client/gui/GuiMerchant",
							mapping?"field_146292_n":"buttonList", "Ljava/util/List;");
					// INVOKESTATIC GuiButton FastTrade.getTradeButton()
					mv.visitMethodInsn(184, 
							"anonym/minecraft/mod/AEventHandler",
							"addTradeButton",
							"(Ljava/util/List;)V", 
							false);
				super.visitCode();
			}
		}
		class MV1 extends MethodVisitor {

			Boolean mapping=false;
			public MV1(int api, MethodVisitor methodVisitor,boolean mapping) {
				super(api, methodVisitor);
				final String tips = "禁止套娃!";
				tips.toString();
				this.mapping = mapping;
			}

			@Override
			public void visitInsn(int opcode) {
				// ARETURN || RETURN
				if (opcode == 176 || opcode == 177) {
					mv.visitVarInsn(25, 1); // ALOAD 1
					// INVOKESTATIC GuiButton FastTrade.getTradeButton()
					mv.visitMethodInsn(184, "anonym/minecraft/mod/AEventHandler", "clickTradeButton",
							"(Lavs;)V", false);
				}
				super.visitInsn(opcode);
			}
		}
	}
}
