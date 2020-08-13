# homework_1 多项式求导

## 整体思路

读入表达式，首先利用正则表达式进行格式判断（分为第一个项和第二个及以后的项，用两种正则表达式匹配）。若格式正确，则利用正则表达式取出每一项，对其进行求导，保存求导后的系数和指数。最后，按照一定格式将求导后的表达式输出。

## 类的说明

### ExpressionDerivation

仅包含main方法，其中仅调用Expression类的读入表达式、表达式格式判断、表达式求导、求导后表达式输出等方法。

### Expression

包含两个成员变量：String类型的表达式，HashMap类型的系数指数

expressionInput方法：读入表达式

expressionFormatCheck方法：表达式格式判断

termMatch方法：匹配表达式中的多项式，对其进行求导，将求导后多项式的系数和指数存储下来

printHashMap方法：将保存的系数和指数按照多项式的格式进行输出

### Term

包含一个成员变量：String类型的多项式

term2Nums：对多项式求导，并返回一个ArrayList，包括求导后的系数和指数