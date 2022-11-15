//后进先出的数据结构，同array来实现栈的数据结构
const stack = [];

//尾部插入
stack.push(1);
stack.push(2);

//pop取出尾部
const item1 = stack.pop();
const item2 = stack.pop();
//push和pop可以实现栈结构

//头部插入
stack.unshift(3);
stack.unshift(4);
//shift取出头部
const item3 = stack.shift();
//unshift shift 实现栈结构
