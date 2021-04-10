
# LivingCityTools 脚本教程
## 一、如何编写脚本？
首先，你需要在你的 `.minecraft` 文件夹内创建一个文件夹 `scripts`，这样，你就完成了脚本文件夹的创建了  
如果你想创建一个脚本，你可以点击鼠标右键，点新建->文本文档，并改名为 `<你的脚本名字>.cts`  
**注意，如果你是Windows用户，你必须要关闭“隐藏已知文件类型的扩展名”再去完成此步，详细方法可以百度**  
我们**非常不建议**你使用 `Notepad++` 来编写你的脚本文件，用 `VSCode` 之类的挺好  
只要不是记事本或者word就行了，就算是写字板也好  

这里，我们创建的脚本是 `example.cts`  
我们的脚本是从上到下执行的，你写什么它就执行什么，但是要按照帮助内含有的命令来编写，  
以下是脚本系统的帮助：  
| 命令 | 描述 |
| ---- | ---- |
| wait [时间] | 等待某段时间后再执行下一条命令  参数:   [时间] - 类型: 整数型，单位:  毫秒 (1秒=1000毫秒)  |
| changeItem [格数] | 在快捷栏中切换物品  参数:  [格数] - 类型: 整数型，取值范围: 0-8 |
| useItemInHand | 使用手中物品 |
| sneak | 潜行一下 |
| openInventory | 打开物品栏 |
| windowClick [格数] [鼠标按钮] [点击模式] | 向服务器发送窗口点击(窗口指的是容器界面)  参数:   [格数] - 类型: 整数型，取值范围: 不固定且从0开始  [鼠标按钮] - 类型: 整数型，取值范围: 0-2 (0-左键 1-右键 2-中键)  [点击模式] - 类型: 整数型，取值范围: 0-8 (我也不刷很懂点击模式，反正1是直接点击) |
| toPlayer [消息] | 在聊天栏输出一条消息，只有你能看见  参数:   [消息] - 类型: 文本型 |
| send [消息] | 发送一条聊天消息  参数:  [消息] - 类型: 文本型 |
| log [消息] | 在你的日志中输出一条消息  参数:  [消息] - 类型: 文本型 |
大小写必须要匹配，这是最重要的  
如果你要写注释，只需要在需要写注释的行的前面加上#即可注释不会被脚本所执行  
你只需要使用命令在脚本上进行随意组合就可以了，这里是一个例子：
```
# Name: Example
# Author: Mr_Xiao_M
changeItem 4
wait 100
useItemInHand
```
这个脚本的意思是：将快捷栏物品转到第5格，然后等待0.1秒，再使用手中的物品  
脚本随意你怎么编写，但不要违反相关规定哦  

## 二、执行脚本
在上一章中，我们学习了如何编写脚本。那么该怎么执行呢？  
很简单，拿我们上一章举的例子 `example.cts` 来说，它的脚本名称就是 `example`  
你只需要在快捷键设置添加快捷键，左侧输入 `script:脚本名称`  
如：`script:example`  
右侧设置好快捷键，只需要在游戏内按下快捷键即可  
  
  
脚本教程就到这里，有什么不懂的可以来问我  
QQ: 2431208142