function [crc] = check_sum(ID,L,pos_vel_sum,MID)
%THIS FUNCTION CALCULATES THE CHECKSUM
%([ID] broadcasting ID 254)
%([L] length of packets)
%(pos_vel_sum is sum of all pos vel)
%(MID is sum of all IDs)

crc=ID+L+131+30+4+pos_vel_sum+MID;
crc=255-bitand(crc,255);
end

