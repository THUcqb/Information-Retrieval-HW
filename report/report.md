# 信息检索大作业第一部分 实验报告

#### 计55 陈齐斌 2015011403

## 1 使用 Lucene 搭建检索系统

### 1.1 实验环境

- vscode
- jdk 1.8
- Maven 3.5.4

### 1.2 检索系统实现

1. 建立索引:

首先写了 Parser 类来解析 CNKI_journal_v2.txt 知网论文数据，存入 Paper 类型。
调用 LuceneIndex 类中的 genIndex() 函数为所有文献建立索引。

2. 检索:

首先载入建立好的索引。
命令行依次接收输入 "题名", "作者", "出版单位", "年"，将查询编码为用于在 Lucene 中查询的字符串格式，然后传给 LuceneQuery 类中的 search 函数来进行查询。
其中按照作者、出版单位、年份进行筛选，之后并根据题目中关键词计算文献的相关分数，并使用 Lucene API 得到排序后的结果。
最后将结果返回为 HTML 格式的表格，用户能够在浏览器中打开 results.html 以查看结果。

### 1.3 样例展示

![demo](images/demo.png)
