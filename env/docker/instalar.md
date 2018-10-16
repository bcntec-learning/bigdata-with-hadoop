# Instalación

Este proyecto Vagrant es base para las pruebas docker.


# Comenzamos

1.  [Descargar e instalar VirtualBox (si es necesario).](https://www.virtualbox.org/wiki/Downloads)
2.  [Descargar e instalar Vagrant (si es necesario).](http://www.vagrantup.com/downloads.html)
3.  Ejecutar ```vagrant plugin install vagrant-vbguest```  para plugin de vboxsf.
4.  Ejecutar ```vagrant plugin install vagrant-docker-compose```  para plugin de docker compose.
5.  Ejecutar ```vagrant up```  para crear la maquina virtual.
6.  Ejecutar ```vagrant ssh``` para ingresar en nuesta maquina virtual.
7.  Ejecutar ```vagrant destroy``` cuando quieras destruir y deshacerte de la máquina virtual.



### Acceso remoto
Mediante  ```vagrant ssh-config  > vagrant-ssh``` podemos exportar la configuracion para acceso remoto via ```ssh```.
Ejemplo  ```ssh -F vagrant-ssh default```, también siendo posbile via putty al puerto guest .

### Comandos utiles de vagrant
```vagrant port```
```vagrant status```
```vagrant suspend```


### Mount
sudo mount -t vboxsf hadoop $HOME/share


### Docker in vagrant
https://www.vagrantup.com/docs/virtualbox/                       vagrant ver


#### Vagrant
[Comandos.](https://www.vagrantup.com/docs/cli/)

