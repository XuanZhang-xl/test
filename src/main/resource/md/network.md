## 网络相关

一. VLAN 参见vlan.md

英语: Virtual LAN 

中文: 虚拟局域网

什么是LAN:
>    LAN可以是由少数几台家用计算机构成的网络，也可以是数以百计的计算机构成的企业网络。
    
什么是VLAN:
>    用于在二层交换机上分割广播域的技术，就是VLAN。通过利用VLAN，我们可以自由设计广播域的构成，提高网络设计的自由度。

什么是广播域:
>    广播域，指的是广播帧（目标MAC地址全部为1）所能传递到的范围，亦即能够直接通信的范围, 比如ARP请求MAC地址就会全局域网广播

为什么需要VLAN
>    如果整个网络只有一个广播域，那么一旦发出广播信息，就会传遍整个网络，并且对网络中的主机带来额外的负担。
