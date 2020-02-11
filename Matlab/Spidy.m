%Robot Dimensions
delete(instrfind);
clear all
close all

%ls6ip='192.168.0.17';
%tls6 = tcpip(ls6ip, 7777, 'NetworkRole', 'client');
%fopen(tls6);
%registerarr=[0,0,2,3,0,0,0,0,0,0];
%fwrite(tls6,registerarr,'uint8');


s = serial('/dev/ttyUSB0');
set(s,'BaudRate',57600);
%fopen(s);
% x_comp = 50;
% y_comp = 85;
% S = 50;
% A = 83;
% W = 150;


x_comp = 80;
y_comp = 80;
S = 40;
A = 72;
W = 150;

n=1;
generateHex
trajectory_variable=1;
g=0;
%Link length array
L=[S,A,W];
 Axis_rotation_across_y_1=0;
 Axis_rotation_across_y_2=0; 
 
%creating trajectory

for trajectory_variable_x=0:0.75:100 
Trajectory_z=50*sin(trajectory_variable_x/32);
Trajectory_x=trajectory_variable_x;
FRMatx(trajectory_variable)=Trajectory_x;
FRMatz(trajectory_variable)=Trajectory_z;
trajectory_variable=trajectory_variable+1;
end

for trajectory_variable=100:-0.25:0
FRMatx(length(FRMatx)+1)=trajectory_variable;
FRMatz(length(FRMatz)+1)=0;
end

trajectory_variable=1;
for trajectory_variable_x=100:-0.75:0
Trajectory_z=50*sin(trajectory_variable_x/32);
Trajectory_x=trajectory_variable_x;
BLMatx(trajectory_variable)=Trajectory_x;
BLMatz(trajectory_variable)=Trajectory_z;
trajectory_variable=trajectory_variable+1;
end

for trajectory_variable=0:0.25:100
BLMatx(length(BLMatx)+1)=trajectory_variable;
BLMatz(length(BLMatz)+1)=0;
end

straight_movement="left";


if straight_movement=="front"
FLMatx = circshift(FRMatx,270);
FLMatz = circshift(FRMatz,270);
BRMatx = circshift(BLMatx,400); 
BRMatz = circshift(BLMatz,400);
BLMatx = circshift(BLMatx,135); 
BLMatz = circshift(BLMatz,135); 
    qw=1;
    er=length(FRMatx);
    we=10;
    Axis_rotation_across_y_1=0; %
    Axis_rotation_across_y_2=0;% 
elseif straight_movement=="back"
    FLMatx = circshift(FRMatx,270);
    FLMatz = circshift(FRMatz,270);
    BRMatx = circshift(BLMatx,400); 
    BRMatz = circshift(BLMatz,400);
    BLMatx = circshift(BLMatx,135); 
    BLMatz = circshift(BLMatz,135); 
    qw=length(FRMatx);
    er=1;
    we=-10;
    Axis_rotation_across_y_1=0; %
    Axis_rotation_across_y_2=0;% 
elseif straight_movement=="left"
    FLMatx = circshift(FRMatx,135);
    FLMatz = circshift(FRMatz,135);
    BRMatx = circshift(FRMatx,270); 
    BRMatz = circshift(FRMatz,270);
    BLMatx = circshift(BLMatx,400); 
    BLMatz = circshift(BLMatz,400);
    FRMatx = circshift(BLMatx,135); 
    FRMatz = circshift(BLMatz,135);
 
    qw=1;
    er=length(FRMatx);
    we=10;
    Axis_rotation_across_y_1=154; %
    Axis_rotation_across_y_2=-154;% 
    
elseif straight_movement=="right"
    FLMatx = circshift(FRMatx,135);
    FLMatz = circshift(FRMatz,135);
    BRMatx = circshift(FRMatx,270); 
    BRMatz = circshift(FRMatz,270);
    BLMatx = circshift(BLMatx,400); 
    BLMatz = circshift(BLMatz,400);
    FRMatx = circshift(BLMatx,135); 
    FRMatz = circshift(BLMatz,135);
    qw=1;
    er=length(FRMatx);
    we=10;
    Axis_rotation_across_y_1=154; %
    Axis_rotation_across_y_2=-154;% 
end
%end of trajectory creation



for p=qw:we:er
FRx=FRMatx(p);
FRy=-230;
FRz=(FRMatz(p))-80;

FLx=FLMatx(p);
FLy=230;
FLz=(FLMatz(p))-100;

BRx=-BRMatx(p);
BRy=-230;
BRz=(BRMatz(p))-100+20;

BLx=-BLMatx(p);
BLy=230;
BLz=(BLMatz(p))-100+20;

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% Compensated coordinates with its axis

FRxc=FRx-50;
FRyc=FRy+85;
FRzc=FRz;

FLxc=FLx-50;
FLyc=FLy-85;
FLzc=FLz;

BRxc=BRx+50;
BRyc=BRy+85;
BRzc=BRz;

BLxc=BLx+50;
BLyc=BLy+85;
BLzc=BLz+0;

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% Compensated coordinates with Front
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% Right leg

FRxcc=FRxc;
FRycc=FRyc;
FRzcc=FRzc;

FLxcc=FLxc;
FLycc=FLyc;
FLzcc=FLzc;

BRxcc=-BRxc;
BRycc=BRyc;
BRzcc=BRzc;

BLxcc=-BLxc;
BLycc=BLyc-170;
BLzcc=BLzc;


%hold on;
scatter3(FLx,FLy,FLz,'O','MarkerFaceColor',[.75 .75 .75]);
scatter3(FRx,FRy,FRz,'O','MarkerFaceColor',[0 .75 0]);
scatter3(BLx,BLy,BLz,'O','MarkerFaceColor',[0 0 .75]);
scatter3(BRx,BRy,BRz,'O','MarkerFaceColor',[.75 0 0]);

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% Front Right Leg

[FRQ] = Spider_leg_ik(FRxcc,FRycc,FRzcc,L,-1); %inverse kinematics
offsetFR=[-pi/2 0 0];          %offset to match up FK and IK
dmt = [-1 -1 -1];           %direction matrix to match up FK and IK
FRQ = FRQ+offsetFR;
FRQ = FRQ.*dmt;

FRJA(1) = FRQ(1);     
FRJA(2) = FRQ(2);
FRJA(3) = FRQ(3);

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% Front Left Leg

[FLQ] = Spider_leg_ik(FLxcc,FLycc,FLzcc,L,-1); %inverse kinematics
offsetFL=[-pi/2 0 0];          %offset to match up FK and IK
dmtFL = [-1 -1 -1];           %direction matrix to match up FK and IK
FLQ = FLQ+offsetFL;
FLQ= FLQ.*dmtFL;

FLJA(1) = FLQ(1);     
FLJA(2) = FLQ(2);
FLJA(3) = FLQ(3);

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% Back Left Leg

[BLQ] = Spider_leg_ik(BLxcc,BLycc,BLzcc,L,-1); %inverse kinematics
offsetBL=[-pi/2 0 0];          %offset to match up FK and IK
dmtBL = [1 1 1];           %direction matrix to match up FK and IK
BLQ = BLQ+offsetBL;
BLQ= BLQ.*dmtBL;

BLJA(1) = BLQ(1);     
BLJA(2) = BLQ(2);
BLJA(3) = BLQ(3);

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% Back Right Leg

[BRQ] = Spider_leg_ik(BRxcc,BRycc,BRzcc,L,-1); %inverse kinematics
offsetBR=[-pi/2 0 0];          %offset to match up FK and IK
dmtBR = [1 1 1];           %direction matrix to match up FK and IK
BRQ = BRQ+offsetBR;
BRQ = BRQ.*dmtBR;


BRJA(1) = BRQ(1);     
BRJA(2) = BRQ(2);
BRJA(3) = BRQ(3);

Spidy_plot(FRJA,FLJA,BLJA,BRJA);

% Model execution

MA1 = FRJA - double([(2*pi)*double(int16(FRJA(1)/(2*pi))) (2*pi)*double(int16(FRJA(2)/(2*pi))) (2*pi)*double(int16(FRJA(3)/(2*pi)))]);
MA1(3)=MA1(3)*(-1);
MA1 = int16(MA1*(180/pi)*(1023/300))+512;
MA1(1)=MA1(1)+154+Axis_rotation_across_y_1;

MA2 = FLJA - double([(2*pi)*double(int16(FLJA(1)/(2*pi))) (2*pi)*double(int16(FLJA(2)/(2*pi))) (2*pi)*double(int16(FLJA(3)/(2*pi)))]);
MA2(1)=MA2(1)*(1);
MA2(2)=MA2(2)*(-1);
MA2 = int16(MA2*(180/pi)*(1023/300))+512;
MA2(1)=MA2(1)-154+Axis_rotation_across_y_2;

MA3 = BLJA - double([(2*pi)*double(int16(BLJA(1)/(2*pi))) (2*pi)*double(int16(BLJA(2)/(2*pi))) (2*pi)*double(int16(BLJA(3)/(2*pi)))]);
MA3(3)=MA3(3)*(1);
MA3(2)=MA3(2)*(-1);
MA3 = int16(MA3*(180/pi)*(1023/300))+512;
MA3(1)=MA3(1)+154+Axis_rotation_across_y_1;

MA4 = BRJA - double([(2*pi)*double(int16(BRJA(1)/(2*pi))) (2*pi)*double(int16(BRJA(2)/(2*pi))) (2*pi)*double(int16(BRJA(3)/(2*pi)))]);
MA4(3)=MA4(3)*(-1);
MA4(2)=MA4(2)*(1);
MA4 = int16(MA4*(180/pi)*(1023/300))+512;
MA4(1)=MA4(1)-154+Axis_rotation_across_y_2;
pos_vec = zeros(1,12);
pos_vec = pos_vec+512;
pos_vec(1:3)  =MA1(1:3);
pos_vec(4:6)  =MA2(1:3);
pos_vec(7:9)  =MA3(1:3);
pos_vec(10:12)=MA4(1:3);


% pos_vec(1)=pos_vec(1)+10;
% pos_vec(2)=pos_vec(2)-0;
% pos_vec(3)=pos_vec(3)-45;
% 
% pos_vec(4)=pos_vec(4)-10;
% pos_vec(5)=pos_vec(5)+0;
% pos_vec(6)=pos_vec(6)+45;
% 
% pos_vec(7)=pos_vec(7)+10;
% pos_vec(8)=pos_vec(8)-0;
% pos_vec(9)=pos_vec(9)-45;
% 
% pos_vec(10)=pos_vec(10)-10;
% pos_vec(11)=pos_vec(11)+0;
% pos_vec(12)=pos_vec(12)+45;
vel_vec = ones(1,12)*512;

dlmwrite(straight_movement+'_pos.txt',pos_vec,'delimiter',',','-append');
dlmwrite(straight_movement+'_vel.txt',vel_vec,'delimiter',',','-append');

[sync_packet] = PACKETS_T3_1(pos_vec,vel_vec);
SP{n}=[sync_packet];
dlmwrite(straight_movement+'.txt',sync_packet,'delimiter',',','-append');
n=n+1;
end

for kl=1:1:3
for k=1:1:length(SP)
%fwrite(s,SP{k});
%packet0=[0,6];
%packet0=horzcat(packet0,SP{k});
%plength=length(packet0);
%lucilength=plength+5;
%lucipacket=[0,0,2,254,0,0,0,0,lucilength,0,0,plength,0,0,0];
%lucipacket=horzcat(lucipacket,packet0);
%disp(lucipacket)
%fwrite(tls6,lucipacket,'uint8');
end
end
%fclose(s);
%fclose(tls6);
%delete tls6;


