function [pos_lb,pos_hb,vel_lb,vel_hb] = low_high_byte(pos_vec,vel_vec,NumberOfMotors)
%THIS FUNCTION GENERATES LOW BYTE AND HIGH BYTE
% ([pos_vec] position vecotr)
% ([vel_vec] velocity vector)
% ([Number of motors])
% ([pos_lb] position lower byte)
% ([pos_hb] position higher byte)
% ([vel_lb] velocity lower byte)
% ([vel_hb] velocity higher byte)

for i=1:NumberOfMotors
    pos_lb(1,i) = (bitand(pos_vec(1,i),255));
    pos_hb(1,i) = (bitsrl(int16(pos_vec(1,i)),8));
    vel_lb(1,i) = (bitand(vel_vec(1,i),255));
    vel_hb(1,i) = (bitsrl(int16(vel_vec(1,i)),8));
end


