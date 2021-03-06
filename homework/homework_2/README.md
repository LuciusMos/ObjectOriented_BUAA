# 题目要求

## 摘要

本次作业，需要完成的任务为**包含简单幂函数和简单正余弦函数的导函数**的求解。

## 问题

### 设定

首先是一些基本概念的声明：

- **带符号整数** 支持前导0的带符号整数，符号可省略，如：` +02`、`-16`、`19260817`等。
- **因子** 
  - **变量因子** 
    - **幂函数** 
      - **一般形式** 由自变量x和指数组成，指数为一个带符号整数，如：`x ^ +2`。
      - **省略形式** 当指数为1的时候，可以采用省略形式，如：`x`。
    - **三角函数** `sin(x)`或`cos(x)`（在本次作业中，括号内仅为x）
      - **一般形式** 类似于幂函数，由`sin(x)`和指数组成，指数为一个带符号整数，如：`sin(x) ^ +2`。
      - **省略形式** 当指数为1的时候，可以采用省略形式，省略指数部分，如：`sin(x)`。
  - **常数因子** 包含一个带符号整数，如：`233`。
- **项** 
  - **一般形式** 由乘法运算符连接若干因子组成，如：`2 * x ^ 2 * 3 * x ^ -2`、`sin(x) * cos(x) * x`。
  - **特殊情况**
    - 第一个因子为常数因子，且值为1的时候，可以省略该常数因子或表示为正号开头的形式，如：`x ^ 2 * x ^ -1`、 `+ x ^ 2`、`+ cos(x) * cos(x)`、 `sin(x) * cos(x) * x`。
    - 第一个因子为常数因子，且值为-1的时候，可以表示为负号开头的形式，如：`-x ^ 2、- cos(x) * sin(x)`。
- **表达式** 由加法和减法运算符连接若干项组成，如： `-1 + x ^ 233 * x ^ 06 - sin(x) * 3 * sin(x)`。此外，**在第一项之前，可以带一个正号或者负号**，如：`- -1 + x ^ 233`、`+ -2 + x ^ 19260817`。注意，**空串不属于合法的表达式**。
- **空白字符** 在本次作业中，空白字符包含且仅包含`<space>`和`\t`。

此外，值得注意的几点是：

- 带符号整数内不允许包含空白字符。
- 三角函数的保留字内不允许包含空白字符，即`sin`和`cos`内不可以含有空白字符。
- 因子、项、表达式，在不与上两条矛盾的前提下，可以在任意位置包含任意数量的空白字符。
- 如果某表达式存在不同的解释方式，则只要有任意一条解释中是合法的，该表达式即为合法。
- **未知数包含且仅包含小写的`x`**。



## 判定

### 输入格式

输入中，包含且仅包含一行，表示一个表达式

### 输出格式

关于输出，首先程序需要对输入数据的合法性进行判定

- 如果是一组合法的输入数据（即符合上述的表达式基本规则），则应当输出一行，表示求出的导函数。格式同样需要满足上述的表达式基本格式规则。
- 如果是一组不合法的输入数据，则应当输出一行`WRONG FORMAT!`

### **判定模式**

#### 正确性判定

对于这次作业结果正确性的判定，在输出符合格式要求的前提下，我们采用和上一次完全一样的方式<del>，可以直接跳至下一段落</del>。具体如下：

- **在区间$ [-10, 10] $上，线性随机选取$1000$个数**，设为$\left\{ x_{i} \right\}$ ($1 \leq i \leq 1000$)
- 设输入多项式为$f\left(x\right)$，其导函数为$f'\left( x \right)$（即正确答案，由MATLAB进行计算），将$\left\{ x_{i} \right\}$依次代入$f'\left(x\right)$，得到结果$\left\{ a_{i} \right\}$
- 设待测输出的多项式为$g'\left(x\right)$，将$\left\{x_i\right\}$依次代入$g'\left(x\right)$，得到结果$\left\{ b_i \right\}$
- 将数列$\left\{a_i\right\}$和数列$\left\{b_i\right\}$依次进行比较，判定每个数是否依次相等
- **如果全部相等，则认为该组输出正确**，否则认为错误
- 考虑到可能会出现随机出的数位于无意义点上导致计算出错，故在上述计算$\left\{b_i\right\}$的过程中，**如果出现计算错误，则将重新生成一组$\left\{x_i\right\}$，并重新计算**。最多将会重试五次，如果重试次数达到上限后依然无法正常计算，则判定该组输出错误。**即，举例说明的话，就是表达式$\frac{x^2}{x}$最终也会被判定为和表达式$x$等价**。(实际上，部分情况下$\left\{a_i\right\}$也可能需要重新计算，不过由$\left\{a_i\right\}$引发的重新计算将不被计算在这五次内)

综上，简而言之，你可以理解为：**只要是和标准结果等价的表达式（允许定义域上的点差异），都会被认定为正确答案**。

#### 性能分判定

在本次作业中，性能分的唯一评判依据，是输出结果的有效长度。

有效长度定义为，输出结果去除所有的空白字符（`<space>`、`\t`）后的长度，设为$L$。

设某同学给出的正确答案的有效长度为$L_p$，所有人目前给出的正确答案的有效长度的最小值为$L_{min}$。

设$x = \frac{L_p}{L_{min}}$，则该同学性能分百分比为：
$$
r(x)= 100 \% \cdot
\begin{cases}
1 & x = 1 \\
122.2893 x^4 - 603.6553 x^3 + 1122.8905 x^2 - 934.0000 x + 293.4754 & 1 < x \leq 1.3 \\
0 & x > 1.3
\end{cases}
$$
简单来说，就是这样：

|  $x$   | $r\left(x\right)$ |
| :----: | :---------------: |
| $1.0$  |     $100.0\%$     |
| $1.05$ |     $59.9\%$      |
| $1.1$  |    $ 35.1 \%$     |
| $1.15$ |     $19.9\%$      |
| $1.2$  |     $10.0\%$      |
| $1.3$  |      $0.0\%$      |

以及，由于格式错误的情况下，输出是固定的，所以实际上对于格式错误的数据点，只要被判定为正确即可获得$100\%$的性能分。

值得注意的是，**获得性能分的前提是，在正确性判定环节被判定为正确**。如果被判定为错误，则性能分部分为0分。

### 互测相关

在互测环节

- **数据的最大长度为100**。（请注意，这里不是有效长度，是去除右侧换行符后的总长度）。

上述限制被定义为**数据基本限制**。在此范围限制内，不作其他任何限制。简而言之

- 如果是格式合法的数据，则被测程序应当给出正确的答案。
- 如果是格式不合法，但是满足上述数据基本限制的话，被测程序应当输出格式不合法情况下的结果。（即输出`WRONG FORMAT!`）
- 如果不满足上述数据基本限制的话，则该数据将被系统忽略，不会对被测程序进行测试。
- 在公测中，也不会存在不满足数据基本限值的数据点。



# 解题思路

