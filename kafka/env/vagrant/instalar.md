# Instalación

Este proyecto Vagrant es base para las pruebas de instalación y ejercicios del curso.

# Comenzamos

3.  Ejecutar ```vagrant plugin install vagrant-vbguest```  para plugin de vboxsf.
3.  Ejecutar ```vagrant up```  para crear la maquina virtual.
4.  Ejecutar ```vagrant ssh``` para ingresar en nuesta maquina virtual.
5.  Ejecutar ```vagrant destroy``` cuando quieras destruir y deshacerte de la máquina virtual.



### Acceso remoto

Via  ```vagrant ssh``` podremos ingresar a la maquina virtual.
Mediante  ```vagrant ssh-config  > vagrant-ssh``` podemos exportar la configuracion para acceso remoto via ```ssh```.
Ejemplo  ```ssh -F vagrant-ssh default```.


### Mount
sudo mount -t vboxsf kafka $HOME/share


#### Vagrant
[Comandos.](https://www.vagrantup.com/docs/cli/)



