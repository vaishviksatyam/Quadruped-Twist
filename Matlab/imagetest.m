u = udp('192.168.0.17',10000,'EnablePortSharing','on');
u.InputBufferSize=25536;
fopen(u);
%fprintf(u,'ok');
fig=figure;
    imgarr=fscanf(u);
    disp(length(imgarr))
    if(length(imgarr)>5000)
        finalarr=imgarr;
        disp('got it')
%     img=imdecode(uint8(imgarr),'jpg');
%     figure(fig);
%     imshow(img);
%     pause(0.1);
%     drawnow;
    end    
fclose(u);
delete(u);
