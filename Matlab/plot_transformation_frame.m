function [] = plot_transformation_frame(f)
Rx=f(:,1);
Ry=f(:,2);
Rz=f(:,3);
Tx=f(1,4);
Ty=f(2,4);
Tz=f(3,4);

s=30;

%plot3([Tx,s*Rx(1)+Tx],[Ty,s*Rx(2)+Ty],[Tz,s*Rx(3)+Tz],'r','linewidth',2);
%plot3([Tx,s*Ry(1)+Tx],[Ty,s*Ry(2)+Ty],[Tz,s*Ry(3)+Tz],'g','linewidth',2);
%plot3([Tx,s*Rz(1)+Tx],[Ty,s*Rz(2)+Ty],[Tz,s*Rz(3)+Tz],'b','linewidth',2);
end