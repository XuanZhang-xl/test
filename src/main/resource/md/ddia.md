# 数据密集型应用系统设计 笔记


## 概念
- 事务
- 赃读
- 脏写
- 读倾斜(不可重复读)
- 幻读, 有三种意义, 只读查询幻读, 以及 读-写幻读
    - 只读查询幻读: 读取到了其他事务插入的数据, rr级隔离级别可以防止, 感觉就是不可重复度
    - 写幻读: 查不到指定主键的数据, 插入指定主键的数据时, 这个主键已经被其他事务所用, 导致插入失败, 由于主键都是自增一般不会出现
    - 读-写幻读(真 * 幻读): 一个事务中的写入改变了另一个事务查询结果, 快照隔离级别只能避免只读查询的幻读, rr级加 for update可以防止

- 更新丢失
    -   防止更新丢失的手段:
    -   原子性操作  update set a = a + 1
    -   显式加锁, 悲观锁  select ... for update
    -   自动检测更新丢失  乐观锁  然而很遗憾, mysql不支持自动检测
- 写倾斜 广义的更新丢失, 两个事务读取同一组对象, 分别更新其中的一部分, 更新操作会影响之前的读取操作,实际上是不能同时更新成功的. 由于写倾斜而导致幻读
    -   显式加锁  select ... for update
    -   自动检测更新丢失  然而很遗憾, mysql不支持自动检测


- 覆盖索引
串行化
- 两阶段加锁(悲观锁)
- 串行化的快照隔离(乐观锁)

## ACID


## 隔离级别
|隔离级别 |	MySQL |	PostgreSQL|
|---|---|---|
|Read Uncommitted |	支持 |	不支持，等价于Read Committed|
|Read Committed |	支持，基于MVCC实现 |	支持，基于MVCC实现|
|Repeatable Read |	支持，基于MVCC实现了Snapshot Isolation，可避免幻读 |	支持，基于MVCC实现了Snapshot Isolation，可避免幻读|
|Serializable |	支持，Repeatable Read + 共享锁 |	支持，基于MVCC实现了Serialized Snapshot Isolation|
|默认级别 |	Repeatable Read |	Read Committed|
|MVCC实现 |	基于Undo Log |	基于B+树直接记录多个版本|

这里 MySQL的Repeatable Read 可避免幻读, 避免的是只读查询幻读

而PostgreSQL的Repeatable Read由于支持了SSI, 可同时避免三种幻读


## TODO
-   如果TCP连接中断, 则事务也必须终止 p218
-   谓词锁 p244
-   