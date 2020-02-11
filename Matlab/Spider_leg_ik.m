function [T] = Spider_leg_ik(x_in,y,z,L,sig)

phi=0;

l0=L(1);
l1=L(2);     
l2=L(3);
l3=0;

t0= atan2((x_in),(y));                                                                    
x=sqrt(((x_in)^2)+((y)^2))-l0;

x1 = x-l3*cos((phi*pi)/180);
z1 = z-l3*sin((phi*pi)/180);

z2 =  -(z1)/sqrt((x1^2)+(z1^2));
x2 =  -(x1)/sqrt((x1^2)+(z1^2));

%pitch angles
t1 = atan2(z2,x2)+ sig*acos(-((x1^2)+(z1^2)+(l1^2)-(l2^2))/((2*l1)*(sqrt((x1^2)+(z1^2)))));
z3 = (z1-l1*sin(t1))/l2;
x3 = (x1-l1*cos(t1))/l2;
t2 = mod(atan2(z3,x3),2*pi)-t1;
t3 = (phi*(pi/180))-(t1+t2);

T=[t0,t1,t2];


end


