## SnowFlake

#### 介绍（Description）

> **SnowFlake** 是笔者编写的基于 **Spring Boot** 应用的一个插件, 该插件能够在一个应用中生成为每一条记录生成一个全局唯一的ID，即便是在分布式应用中。该插件以 **spring-boot-starter** 的形式实现了 **Twitter** 提出的 **SnowFlake** 算法，便于用户直接在 Spring boot 项目中直接使用。
>
> 在 **SnowFlake** 中，笔者为生成器（Generator）编写了三种实现方式，分别是 **DefaultGenerator**、**CachedGenerator** 和 **AtomicGenerator**，其中：
>
> * **DefaultGenerator** 是生成器接口的默认实现，即默认支持的生成器。该生成器采用**显示锁**实现，是SnowFlake算法的基本实现形式。
> * **CachedGenerator** 在 **DefaultGenerator** 的基础上，使用 **ConcurrentLinkedQueue** 作为并发缓存容器实现并发场景下安全获取生成的id，即该生成器是线程安全的生成器。
> * **AtomicGenerator** 则采用 **AtomicStampedReference** 作为实现工具。相比较前面两种实现方式，**AtomicGenerator** 的实现采用 **CAS + 自旋** 的方式达到了 **无锁** 的实现，具有明显的改进。

> SnowFlake is a plugin based on spring application, which can generate a global unique id for each record in your database, even in distributed application. This plugin implements the SnowFlake algorithm proposed by Twitter in a spring-boot-starter manner such that user(s) can use this plugin in his/her spring-boot application easily.
>
> There are three kinds of implementations of interface **Generator**, namely **DefaultGenerator**, **CachedGenerator** and **AtomicGenerator**. Among them,
>
> * **DefaultGenerator** is the default implementation of **Generator** interface by using explicit lock like **StampLock**, regarding as the basic implementation of interface **Generator**.
> * **CachedGenerator** adopts **ConcurrentLinkedQueue** as a concurrent cached container to acquire identity based on **DefaultGenerator**, namely it's a **thread-safe** implementation.
> * **AtomicGenerator** Generates identities using **AtomicStampedReference** as the tools. It is **Lock Free** by using **CAS + Spin** than **DefaultGenerator** and **CachedGenerator**.

***

#### 版本（Versions）

##### V2.0.1

> V2.0.1 将 **AtomicGenerator** 的实现由 **AtomicLong** 切换成了 **AtomicStampedReference**。

##### V2.0.0

>  V2.0.0 添加了 **CachedGenerator** 和 **AtomicGenerator** 两种生成器实现。

##### V1.0.1

> V1.0.1 添加了对 **spring-boot-configuration-processor** 的支持，支持IDE提示配置信息。

##### V1.0.0

> V1.0.0 基本实现 **SnowFlake** 算法，并添加了对 **spring-boot** 的友好支持。

***

#### 项目组成（Items）

| Gitee                                                  | Github                                                 |
| ------------------------------------------------------ | ------------------------------------------------------ |
| [Gitee:SnowFlake](https://gitee.com/lemonpy/SnowFlake) | [Github:SnowFlake](https://github.com/Zon-g/SnowFlake) |

**For more information, please go to the wiki in the repository**.

如要查看更多信息，请跟随之 wiki 页面。

***

#### 参与贡献（Contribution）

> 1.  Fork 本仓库。
> 2.  新建 Feat_xxx 分支。
> 3.  提交代码。
> 4.  新建 Pull Request。

> 1.  Fork the repository.
> 2.  Create Feat_xxx branch.
> 3.  Commit your code.
> 4.  Create Pull Request.

***

#### 致谢（Thanks）

> 感谢 Jetbrains 提供的学生版全家桶。

> Here, I appreciate that Jetbrains provides product pack for student.

***

