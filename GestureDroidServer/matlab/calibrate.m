function [caldata] = calibrate(data,calibrationdata,treshold)

cal=calibrationdata;

% cal1 abziehen der mittelwerte und mitelwert
cal(1,:)= [cal(1,[1 2])-cal(1,3),cal(1,3)];
cal(2,:)= [cal(2,[1 2])-cal(2,3),cal(2,3)];
cal(3,:)= [cal(3,[1 2])-cal(3,3),cal(3,3)];
cal1=cal


cal=calibrationdata;
% cal2 mittelwerte koennten ja auch differenzen der abweichung sein
cal(1,3)=(cal(1,1)-cal(1,2))/2-cal(1,1);
cal(2,3)=(cal(2,1)-cal(2,2))/2-cal(2,1);
cal(3,3)=(cal(3,1)-cal(3,2))/2-cal(3,1);
cal2=cal;

% mittlere Erdbescheunigung g in m/s^2 Beschleunigung
g=mean(cal);
g=mean([g(1) -g(2)]);
g=9.80665;

%% nun wir richtig kallibriert
cal=calibrationdata;

% N1 spezifisch
cal(3,3)=cal(3,3)+0.025;
cal(2,3)=cal(2,3)-0.034;
cal(1,3)=cal(1,3)+0.072;

cal(3,1:2)=cal(3,1:2)-0.5;
cal(1,1:2)=cal(1,1:2)-0.35;



data(:,2)=data(:,2)+cal(1,3);
data(:,3)=data(:,3)+cal(2,3);
data(:,4)=data(:,4)+cal(3,3);




% +x
data(data(:,2)>0,2)=data(data(:,2)>0,2).*(1/cal(1,1));
% -x
data(data(:,2)<0,2)=data(data(:,2)<0,2).*-(1/cal(1,2));
% 
% +y
data(data(:,3)>0,3)=data(data(:,3)>0,3).*(1/cal(2,1));
% -y
data(data(:,3)<0,3)=data(data(:,3)<0,3).*-(1/cal(2,2));

% +z
data(data(:,4)>0,4)=data(data(:,4)>0,4).*(1/cal(3,1));
% -z
data(data(:,4)<0,4)=data(data(:,4)<0,4).*-(1/cal(3,2));

data(:,2)=data(:,2).*g;
data(:,3)=data(:,3).*g;
data(:,4)=data(:,4).*g;

% data(data(:,2)<treshold & data(:,2)>-treshold ,2)=0;
% data(data(:,3)<treshold & data(:,3)>-treshold ,3)=0;
% data(data(:,4)<treshold & data(:,4)>-treshold ,4)=0;


caldata=data;



end