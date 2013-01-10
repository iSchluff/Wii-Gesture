function [bestcalibration] = getcalibrateData(calibratefile)
% Berechnet min max und durchschnittliche Abweichung von x y z
% minX maxX deltaX
% minY maxY deltaY
% minZ maxZ deltaZ

n1=load(calibratefile);
data=n1(:,2:7);

delta=0.2;

clear calibration

bestcalibration = zeros(3,6);
c=0;
for d=delta:delta:0.7;
    
    c=c+1;
    
    for i=1:3

     part = [1 2 3];
     part(i)=[];

        max=sortrows( data(:,1:6) ,i);
        max=max( max(:,i)>5,:);% alle x beschleunigungen >5
        max=max((abs(max(:,part(1))).*abs(max(:,part(2))))<d,:);% produkt von y z <d
%         q25=quantile(max(:,i),0.25);
%         q75=quantile(max(:,i),0.75);
%         max=max(max(:,i)<=q75 & max(:,i)>=q25,:);% 25% 75% quantil abschnitt

        calibration(i,1)=median(max(:,i));
        calibration(i,2)=median(max(:,part(1)));
        calibration(i,3)=median(max(:,part(2)));


        
        
        min=sortrows( data(:,1:6) ,i);
        min=min( min(:,i)<5,:);% alle x beschleunigungen <5
        min=min((abs(min(:,part(1))).*abs(min(:,part(2))))<d,:);% produkt von y z <d
%         q25=quantile(min(:,i),0.25);
%         q75=quantile(min(:,i),0.75);
%         min=min(min(:,i)<=q75 & min(:,i)>=q25,:);% 25% 75% quantil abschnitt

        calibration(i,4)=median(min(:,i));
        calibration(i,5)=median(min(:,part(1)));
        calibration(i,6)=median(min(:,part(2)));
        
    end
    
    % die Beschleunigungen sollen matimal sein und die Abweichungen minimal
    % (10*) weil die Abweichungen ja eigentlich 0 sein sollten und ich eine
    % Abweichung von 0.1 so schlecht rechne wie bei der Beschleunigung 10
    % gut
    calx= (calibration(1,1)-calibration(1,4))-10*sum(abs(calibration(1,[2 3 5 6])));
    caly= (calibration(2,1)-calibration(2,4))-10*sum(abs(calibration(2,[2 3 5 6])));
    calz= (calibration(3,1)-calibration(3,4))-10*sum(abs(calibration(3,[2 3 5 6])));
    
    bestcalx= (bestcalibration(1,1)-bestcalibration(1,4))-10*sum(abs(bestcalibration(1,[2 3 5 6])));
    bestcaly= (bestcalibration(2,1)-bestcalibration(2,4))-10*sum(abs(bestcalibration(2,[2 3 5 6])));
    bestcalz= (bestcalibration(1,1)-bestcalibration(3,4))-10*sum(abs(bestcalibration(3,[2 3 5 6])));

    if(bestcalx<calx)
        bestcalibration(1,:)=calibration(1,:);
    end
    
    if(bestcaly<caly)
        bestcalibration(2,:)=calibration(2,:);
    end
    
    if(bestcalz<calz)
        bestcalibration(3,:)=calibration(3,:);
    end


   
end

% Berechnung der mittlere Abweichung in Ruhelage
b=bestcalibration;

deltax=median([b(2,2) b(2,5) b(3,2) b(3,5)]);
deltay=median([b(1,2) b(1,5) b(3,3) b(3,6)]);
deltaz=median([b(1,3) b(1,6) b(2,3) b(2,6)]);

bestcalibration=bestcalibration(:,[1 4]);
bestcalibration(1,3)=-deltax;
bestcalibration(2,3)=-deltay;
bestcalibration(3,3)=-deltaz;


end