package email.com.gmail.coolxiaom95.citytools.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import email.com.gmail.coolxiaom95.citytools.Main;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;

public class Util {
	public static float strToFloat(String s, float nullValue) {
		try {
			return Float.parseFloat(s);
		}catch(Throwable t) {
			return nullValue;
		}
	}
	public static String clearColor(String text) {
		return text
				.replace("§a", "")
				.replace("§b", "")
				.replace("§c", "")
				.replace("§d", "")
				.replace("§e", "")
				.replace("§f", "")
				.replace("§r", "")
				.replace("§l", "")
				.replace("§m", "")
				.replace("§n", "")
				.replace("§o", "")
				.replace("§k", "")
				.replace("§0", "")
				.replace("§1", "")
				.replace("§2", "")
				.replace("§3", "")
				.replace("§4", "")
				.replace("§5", "")
				.replace("§6", "")
				.replace("§7", "")
				.replace("§8", "")
				.replace("§9", "")
				.replace("§A", "")
				.replace("§B", "")
				.replace("§C", "")
				.replace("§D", "")
				.replace("§E", "")
				.replace("§F", "")
				.replace("§R", "")
				.replace("§L", "")
				.replace("§M", "")
				.replace("§N", "")
				.replace("§O", "")
				.replace("§K", "");
	}
	/**
	 * 一行一行地读取文件
	 */
	public static List<String> readFile(String path) throws IOException {
		List<String> result = new ArrayList<String>();
		;
		File file = new File(path);

		FileReader reader = new FileReader(file);
		BufferedReader br = new BufferedReader(reader);

		while (br.ready()) {
			result.add(br.readLine());
		}

		reader.close();
		br.close();

		return result;
	}

	/**
	 * 写入内容到文件
	 */
	public static void writeFile(String path, String content) {
		BufferedWriter out = null;
		try {
			File writename = new File(path);
			writename.createNewFile();
			out = new BufferedWriter(new FileWriter(writename));
			out.write(content);
			out.flush();
			out.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取玩家皮肤
	 * 
	 * 出自 白玉楼之梦 示例代码
	 */
	public static ResourceLocation getSkin(String name) {
		ResourceLocation resourceLocation = Main.skinCache.get(name);
		if (resourceLocation == null) {
			resourceLocation = new ResourceLocation("skins/" + StringUtils.stripControlCodes(name));
			AbstractClientPlayer.getDownloadImageSkin(resourceLocation, name);

			Main.skinCache.put(name, resourceLocation);
		}
		return resourceLocation;
	}

	/**
	 * 出自 GuiScreen.java
	 */
	public static void sendChatMessage(String msg, boolean addToChat) {
		if (Minecraft.getMinecraft() != null) {
			if (Minecraft.getMinecraft().thePlayer != null) {
				if (addToChat) {
					Minecraft.getMinecraft().ingameGUI.getChatGUI().addToSentMessages(msg);
				}
				if (net.minecraftforge.client.ClientCommandHandler.instance
						.executeCommand(Minecraft.getMinecraft().thePlayer, msg) != 0)
					return;

				Minecraft.getMinecraft().thePlayer.sendChatMessage(msg);
			}
		}
	} public static String ImageToBase64ByOnline(String imgURL) {
	      ByteArrayOutputStream data = new ByteArrayOutputStream();
	      try {
	         // 创建URL
	         URL url = new URL(imgURL);
	         byte[] by = new byte[1024];
	         // 创建链接
	         HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	         conn.setRequestMethod("GET");
	         conn.setConnectTimeout(5000);
	         InputStream is = conn.getInputStream();
	         // 将内容读取内存中
	         int len = -1;
	         while ((len = is.read(by)) != -1) {
	            data.write(by, 0, len);
	         }
	         // 关闭流
	         is.close();
	      } catch (IOException e) {
	         e.printStackTrace();
	         return "error";
	      }
	      // 对字节数组Base64编码
	      return Base64.getEncoder().encodeToString(data.toByteArray());
	   }
	   public static String requestByGetMethod(String url) {
	        CloseableHttpClient httpClient = HttpClients.createDefault();
	        StringBuilder entityStringBuilder = null;
	        try {
	            HttpGet get = new HttpGet(url);
	            CloseableHttpResponse httpResponse = null;
	            httpResponse = httpClient.execute(get);
	            try {
	                HttpEntity entity = httpResponse.getEntity();
	                entityStringBuilder = new StringBuilder();
	                if (null != entity) {
	                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent(), "UTF-8"), 8 * 1024);
	                    String line = null;
	                    while ((line = bufferedReader.readLine()) != null) {
	                        entityStringBuilder.append(line + "/n");
	                    }
	                }
	            } finally {
	                httpResponse.close();
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        } finally {
	            try {
	                if (httpClient != null) {
	                    httpClient.close();
	                }
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	        return entityStringBuilder.toString();
	    }
	public static String sendPost(String url, Map<String,Object> params) {  
        PrintWriter out = null;  
        BufferedReader in = null;  
        String result = "";  
        try {  
  		  StringBuilder postData = new StringBuilder();
  		  for (Map.Entry<String,Object> param : params.entrySet()) {
  		      if (postData.length() != 0) postData.append('&');
  		      postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
  		      postData.append('=');
  		      postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
  		  }
  		  System.out.println(postData);
            URL realUrl = new URL(url);  
            // 打开和URL之间的连接  
            
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();  
            // 10秒超时
            conn.setReadTimeout(10000);
            // 方法为 POST
            conn.setRequestMethod("POST");
            // 禁用缓存
            conn.setUseCaches(false);
            // 设置通用的数据
            conn.setRequestProperty("accept", "application/json");  
            conn.setRequestProperty("connection", "Keep-Alive");  
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");  
            
            conn.setDoOutput(true);// 发送POST请求必须设置如下两行  
            conn.setDoInput(true);  
            
            out = new PrintWriter(conn.getOutputStream());// 获取URLConnection对象对应的输出流s  
            out.print(postData);// 发送请求参数  
            out.flush();// flush输出流的缓冲  
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));// 定义BufferedReader输入流来读取URL的响应  
            String line;  
            while ((line = in.readLine()) != null) {  
                result += "\n" + line;  
            }  
        } catch (Exception e) {  
            System.out.println("发送POST请求出现异常！" + e);  
            e.printStackTrace();  
        }  
        // 使用finally块来关闭输出流、输入流  
        finally {  
            try {  
                if (out != null) {  
                    out.close();  
                }  
                if (in != null) {  
                    in.close();  
                }  
            } catch (IOException ex) {  
                ex.printStackTrace();  
            }  
        }  
        return result;  
    }
	/**
	 * 往玩家的聊天栏添加一条消息
	 */
	public static void addMessage(String message) {
		if (Minecraft.getMinecraft() != null) {
			Minecraft mc = Minecraft.getMinecraft();
			if (mc.thePlayer != null) {
				mc.thePlayer.addChatMessage(new ChatComponentText(message));
			}
		}
	}

	public static void bindTexture(ResourceLocation texture) {
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
	}

	public static void drawRectangle(int rX, int rY, int rWidth, int rHeight, float a, float r, float g, float b) {
		int left = rX;
		int top = rY;
		int right = rX + rWidth;
		int bottom = rY + rHeight;
		drawRect(left, top, right, bottom, a, r, g, b);
	}

	/**
	 * 在屏幕上绘制一个矩形
	 * 
	 * @param left   矩形左边的坐标
	 * @param top    矩形上边的坐标
	 * @param right  矩形右边的坐标
	 * @param bottom 矩形下边在坐标
	 * @param a      矩形的不透明度
	 * @param r      矩形的颜色-红色通道
	 * @param g      矩形的颜色-绿色通道
	 * @param b      矩形的颜色-蓝色通道
	 */
	public static void drawRect(int left, int top, int right, int bottom, float a, float r, float g, float b) {

		if (left < right) {
			int i = left;
			left = right;
			right = i;
		}

		if (top < bottom) {
			int j = top;
			top = bottom;
			bottom = j;
		}

		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		GlStateManager.enableBlend();
		GlStateManager.disableTexture2D();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GlStateManager.color(r/255f, g/255f, b/255f, a);
		worldrenderer.begin(7, DefaultVertexFormats.POSITION);
		worldrenderer.pos((double) left, (double) bottom, 0.0D).endVertex();
		worldrenderer.pos((double) right, (double) bottom, 0.0D).endVertex();
		worldrenderer.pos((double) right, (double) top, 0.0D).endVertex();
		worldrenderer.pos((double) left, (double) top, 0.0D).endVertex();
		tessellator.draw();
		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();

		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
	}
    public static void drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height)
    {
    	drawTexturedModalRect(x,y,textureX, textureY,width,height,0);
    }
    public static void drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height, double zIndex)
    {
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos((double)(x + 0), (double)(y + height), zIndex).tex((double)((float)(textureX + 0) * f), (double)((float)(textureY + height) * f1)).endVertex();
        worldrenderer.pos((double)(x + width), (double)(y + height), zIndex).tex((double)((float)(textureX + width) * f), (double)((float)(textureY + height) * f1)).endVertex();
        worldrenderer.pos((double)(x + width), (double)(y + 0), zIndex).tex((double)((float)(textureX + width) * f), (double)((float)(textureY + 0) * f1)).endVertex();
        worldrenderer.pos((double)(x + 0), (double)(y + 0), zIndex).tex((double)((float)(textureX + 0) * f), (double)((float)(textureY + 0) * f1)).endVertex();
        tessellator.draw();
    }

	public static ArrayList<String> getScoreboard() {
		if (Minecraft.getMinecraft() != null) {
			Minecraft mc = Minecraft.getMinecraft();
			if (mc.theWorld != null) {
				Scoreboard scoreboard = Minecraft.getMinecraft().theWorld.getScoreboard();
				ScoreObjective scoreobjective = null;
				if (scoreboard != null && mc.thePlayer != null) {
					ScorePlayerTeam scoreplayerteam = scoreboard
							.getPlayersTeam(Minecraft.getMinecraft().thePlayer.getName());

					if (scoreplayerteam != null) {
						int i1 = scoreplayerteam.getChatFormat().getColorIndex();

						if (i1 >= 0) {
							scoreobjective = scoreboard.getObjectiveInDisplaySlot(3 + i1);
						}
					}

					ScoreObjective scoreobjective1 = scoreobjective != null ? scoreobjective
							: scoreboard.getObjectiveInDisplaySlot(1);

					Collection<Score> collection = scoreboard.getSortedScores(scoreobjective1);
					List<Score> list = Lists.newArrayList(Iterables.filter(collection, new Predicate<Score>() {
						public boolean apply(Score p_apply_1_) {
							return p_apply_1_.getPlayerName() != null && !p_apply_1_.getPlayerName().startsWith("#");
						}
					}));

					if (list.size() > 15) {
						collection = Lists.newArrayList(Iterables.skip(list, collection.size() - 15));
					} else {
						collection = list;
					}

					int i = mc.fontRendererObj.getStringWidth(scoreobjective1.getDisplayName());

					for (Score score : collection) {
						ScorePlayerTeam scoreplayerteam1 = scoreboard.getPlayersTeam(score.getPlayerName());
						String s = ScorePlayerTeam.formatPlayerName(scoreplayerteam1, score.getPlayerName()) + ": "
								+ EnumChatFormatting.RED + score.getScorePoints();
						i = Math.max(i, mc.fontRendererObj.getStringWidth(s));
					}
					ArrayList<String> temp = new ArrayList<String>();
					for (Score score1 : collection) {
						ScorePlayerTeam scoreplayerteam1 = scoreboard.getPlayersTeam(score1.getPlayerName());
						String s1 = ScorePlayerTeam.formatPlayerName(scoreplayerteam1, score1.getPlayerName());
						temp.add(s1);
					}
					ArrayList<String> result = new ArrayList<String>();

					for (int j = temp.size() - 1; j > -1; j--) {
						result.add(temp.size() - 1 - j + " : " + temp.get(j));
					}

					return result;
				}
			}
		}
		return new ArrayList<String>();
	}
}
