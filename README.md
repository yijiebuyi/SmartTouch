# SmartTouch
##SmartTouch是一款基于UiAutomator框架，自动模拟用户手势操作的app，类似于触摸精灵，按键精灵。

SmartTouch是一个android工程，而SmartTouchLib是一个java工程。SmartTouchLib是脚本的核心运行库，它封装了UiAutomator的操作（如模拟back键，点击某个地方，打开最近的应用），上层的开发者可以编写lua脚本执行相应的操作，而lua的方法库定义在luaScript类中，具体的实现是在luaScriptImpl类中。更多的lua语音的学习参照![luajava](https://github.com/jasonsantos/luajava)。SmartTouchLib是一个本地进程，SmartTouch应通过socket和它进行通信。ps：UiAutomator框架需运行在android 4.4以上的版本。

1.生成核心库.jar文件
  a.通过eclipse导入SmartTouchLib的java工程
  b.修改SmartTouchLib目录下的local.properties文件，设置android sdk的安装根路径
  c.以Java Application方式运行CaseMain类，会在控制台找到生产的.jar文件

2.将生成的.jar文件拷贝到SmartTouch的assets目录下面

3.SmartTouch的assets目录下面libluajava.so，lualibs.lua，test.lua分别是luajava的so库，lua脚本的函数库以及脚本lua文件，test.lua的函数必须来自于lualibs.lua中，否则会导致执行失败。

lua 执行脚本

![](https://raw.githubusercontent.com/yijiebuyi/SmartTouch/master/lua_script.png)


执行效果 ps：gif文件有点大，最好下载下来本地查看

![](https://raw.githubusercontent.com/yijiebuyi/SmartTouch/master/preview.gif)



