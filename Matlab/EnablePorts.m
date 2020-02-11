function [] = EnablePorts()
delete(instrfind);
clear all
close all

ls6ip='192.168.0.14';
tls6 = tcpip(ls6ip, 7777, 'NetworkRole', 'client');
fopen(tls6);
registerarr=[0,0,2,3,0,0,0,0,0,0];
fwrite(tls6,registerarr,'uint8');


s = serial('COM20');
set(s,'BaudRate',57600);

end

