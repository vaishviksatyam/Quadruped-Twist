function [outputArg1,outputArg2] = Spidy_plot(FRJA,FLJA,BLJA,BRJA)
%UNTITLED2 Summary of this function goes here
%   JA-joint angles

x_comp = 50;
y_comp = 85;
S = 50;
A = 83;
W = 150;

%Forward kinematics
FC = T(0,0,0);
F1 =  FC * T(x_comp,-y_comp,0)*RZ(FRJA(1));
F2 =  F1 * T(S,0,0)*RY(FRJA(2));
F3 =  F2 * T(A,0,0)*RY(FRJA(3));
FE1 = F3 * T(W,0,0);

FC = T(0,0,0);
F4 =  FC * T(x_comp,y_comp,0)*RZ(FLJA(1));
F5 =  F4 * T(S,0,0)*RY(FLJA(2));
F6 =  F5 * T(A,0,0) * RY(FLJA(3));
FE2 = F6 * T(W,0,0);


FC = T(0,0,0);
F7 =  FC * T(-x_comp,y_comp,0)*RZ(BLJA(1));
F8 =  F7 * T(-S,0,0)*RY(BLJA(2));
F9 =  F8 * T(-A,0,0)*RY(BLJA(3));
FE3 = F9 * T(-W,0,0);

FC = T(0,0,0);
F10 =  FC * T(-x_comp,-y_comp,0)*RZ(BRJA(1));
F11 =  F10 * T(-S,0,0)*RY(BRJA(2));
F12 =  F11 * T(-A,0,0) * RY(BRJA(3));
FE4 = F12 * T(-W,0,0);

Fd =  FC * T(x_comp+30,0,0)*RZ(0);
%Leg 1
plot_line(F1,F4);
hold on;
plot_line(F1,F2);
plot_line(F2,F3);
plot_line(F3,FE1);

%Leg 2
plot_line(F4,F7);
plot_line(F4,F5);
plot_line(F5,F6);
plot_line(F6,FE2);

%Leg 3
plot_line(F7,F10);
plot_line(F7,F8);
plot_line(F8,F9);
plot_line(F9,FE3);

%Leg 4
plot_line(F10,F1);
plot_line(F10,F11);
plot_line(F11,F12);
plot_line(F12,FE4);


plot_line(FC,Fd);

plot_transformation_frame(F1); %plots the transformation frame
plot_transformation_frame(F2); %plots the transformation frame
plot_transformation_frame(F3); %plots the transformation frame
plot_transformation_frame(F4); %plots the transformation frame
plot_transformation_frame(F5); %plots the transformation frame
plot_transformation_frame(F6); %plots the transformation frame
plot_transformation_frame(F7); %plots the transformation frame
plot_transformation_frame(F8); %plots the transformation frame
plot_transformation_frame(F9); %plots the transformation frame
plot_transformation_frame(F10); %plots the transformation frame
plot_transformation_frame(F11); %plots the transformation frame
plot_transformation_frame(F12); %plots the transformation frame
plot_transformation_frame(FE1); %plots the transformation frame
plot_transformation_frame(FE2); %plots the transformation frame
plot_transformation_frame(FE3); %plots the transformation frame
plot_transformation_frame(FE4); %plots the transformation frame
plot_transformation_frame(FC); %plots the transformation frame

axis equal;
xlabel('x')
ylabel('y')
zlabel('z')


end

