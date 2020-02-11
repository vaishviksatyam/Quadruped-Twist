%udps = udp('192.168.0.17',10000,'EnablePortSharing','on','LocalPort',12345,'LocalIPPortSource','Property');
%udps=dsp.UDPSender('RemoteIPPort',10000,'LocalIPPort',34567,'RemoteIPAddress','192.168.0.17','LocalIPPortSource','Property');
udpr = dsp.UDPReceiver('LocalIPPort',10000);
% To prevent the loss of packets, call the |setup| method
% on the object before the first call to the |step| method.
setup(udpr); 
%fopen(udps);

%fwrite(udps,(uint8([0,0])));
%udps(uint8([0,0]))
while(1)
    dataReceived=udpr();
    disp(length(dataReceived));
end

%release(udps);
release(udpr);