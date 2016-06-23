function newObject()
    btn = luajava.newInstance("com.cs.lua.luaScriptImpl");
end

--进行毫秒级别延迟
function sleep(time)
  btn:sleep(time);
end

--点击home键
function home()
  btn:home();
end

--返回键
function back()
  btn:back();
end

--菜单键
function menu()
  btn:menu();
end

--确定键
function enter()
  btn:enter();
end


--删除键
function delete()
  btn:delete();
end

--模拟短按最近使用程序
function recentApps()
  btn:recentApps();
end

function search()
  btn:search();
end

function clickname(name)
  btn:clickname(name);
end

function click(x, y)
  btn:click(x, y);
end

function clickclass(name)
  btn:clickclass(name);
end

function drag(startx, starty, endx, endy, step)
  btn:drag(startx,starty,endx,endy,step);
end

function swipe(startx, starty, endx, endy, step)
  btn:swipe(startx, starty, endx, endy, step);
end

function orientationLeft()
  btn:orientationLeft();
end


function orientationNatural()
  btn:orientationNatural();
end


function orientationRight()
  btn:orientationRight();
end

function wakeUp()
  btn:wakeUp();
end

function closesleep()
  btn:closesleep();
end


function takeScreenshot(path)
  btn:takeScreenshot(path);
end


function openNotification()
  btn:openNotification();
end


function openQuickSettings()
  btn:openQuickSettings();
end


function classedittext(classname, content)
  btn:classedittext(classname, content);
end

function textedittext(classname, content)
  btn:textedittext(classname, content);
end

function keycode(keycode, uc)
  btn:keycode(keycode, uc);
end

function getColor(x, y)
  btn:getColor(x, y);
end