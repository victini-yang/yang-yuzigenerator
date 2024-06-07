# acm-template-pro-generator

> ACM 示例模板生成器
>
> 作者：Victiny
>
> 基于 [程序员鱼皮](https://yuyuanweb.feishu.cn/wiki/Abldw5WkjidySxkKxU2cQdAtnah) 的 [鱼籽代码生成器项目](https://github.com/liyupi/yuzi-generator) 制作，感谢您的使用！

可以通过命令行交互式输入的方式动态生成想要的项目代码

## 使用说明

执行项目根目录下的脚本文件：

```
generator <命令> <选项参数>
```

示例命令：

```
generator generate -n -l 
```

## 参数说明

1）needGit

类型：boolean

描述：是否生成.gitignore文件

默认值：true

缩写： -n

2）loop

类型：boolean

描述：是否生成循环

默认值：false

缩写： -l

