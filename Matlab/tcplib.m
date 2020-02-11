ls6ip='192.168.0.3';
t = tcpip(ls6ip, 7777, 'NetworkRole', 'client');
fopen(t);
registerarr=[0,0,2,3,0,0,0,0,0,0];
fwrite(t,registerarr,'uint8');
pause(5);
fclose(t);
delete(t);