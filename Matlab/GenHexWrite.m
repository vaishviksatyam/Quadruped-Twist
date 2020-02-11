function [MA] = Spidy(MA)
generateHex

pos_vec = zeros(1,12);
pos_vec = pos_vec+512;

pos_vec(1:3)=MA(1:3);
vel_vec = ones(1,12)*50;

[sync_packet] = PACKETS_T3_1(pos_vec,vel_vec);

s = serial('COM20');
set(s,'BaudRate',57600);
fopen(s);
fwrite(s,sync_packet);
pause(0.2);
fclose(s);
end