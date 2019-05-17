import csv
import numpy as np
import matplotlib.pyplot as plot
import math
import time
import datetime

class Congestion:
  #stationID: int, stationName: string, date : 0~365
  def __init__(self):
     self.stationIDs, self.stationNames = self.readStationIDName()
     for date in range(365):
        self.congestion(self.stationIDs,self.stationNames, date)
     
  def readStationIDName(self):
    temp=[]
    addressData = []
    with open("stationID.csv", newline='') as csvfile:
      address = csv.reader(csvfile,delimiter='\t')
      for row in address:
        temp.append(row)
      for row in temp:
        rowtemp = (row)[0].split(",")
        addressData.append(rowtemp)
      stationIDs = []
      stationNames = []
      for row in addressData:
#          print(row)
          stationIDs.append(row[2])
          stationNames.append(row[0])
    #  print(stationIDs)
    #  print(stationNames)
      return stationIDs, stationNames

  
    ## Read CSV file and make lists
  def readCSV(self, stationID, stationName, wantTime):
    temp = []
    csvData = []
    xCoded = []
    y = []
    with open('./2011_2014_station_con_weather - only_congestion/'+str(stationID)+'_'+str(stationName)+'_'+str(wantTime)+'_congestion.csv',newline='') as csvfile:
      spamreader = csv.reader(csvfile, delimiter='\t')
      for row in spamreader:
        temp.append(row)
    ## csvData[0] : row 0 of csv
    ## (csvData[0])[0]: ele (0,0) of csv 
      for row in temp:
        rowtemp = (row)[0].split(",")
    #   print(rowtemp)
        csvData.append(rowtemp)
    ## index ;  x of graph ; 0~365
    index = 0
    weekDay = []
    weekDayY = []
    saturDay = []
    saturDayY = []
    redDay = []
    redDayY = []

    for row in csvData:
        #skip 2/29
        if(row[1] == '2' and row[2] == '29') :
     #       print("2/29!!! pass!!!", index)
            continue
        #make index 0 when YYYY-01-01
        if(row[1] == '1' and row[2] == '1') :
     #       print("index to be 0!! ", index)
            index = 0
     #       print("index to be 0!! ", index)
        #default : weekday [0] = 0.0, [1] = 0.0
        if(row[4] == '월' or row[4] =='화' or row[4] =='수' or row[4] =='목' or row[4] =='금'): weekDay.append([index]);weekDayY.append(row[7])
        elif(row[4] == '토'): saturDay.append([index]);saturDayY.append(row[7])
        elif(row[4] == '일' or row[4] == '공'): redDay.append([index]);redDayY.append(row[7])
        index = index + 1

    return weekDay, weekDayY, saturDay, saturDayY, redDay, redDayY

  ### add x0 (=1) in front of rows
  def insertFirstColumn(self, xCoded):
    toBeNewX= xCoded.copy()
    for row in toBeNewX:
        row.insert(0,1)
    return toBeNewX

  ###get theta
  def getTheta(self,xCoded,y):
  ## make array x to matrix ; newX  (m*n+1) matrix
  ## make array y to matrix ; newY (m*1) matrix

   # print("xCoded[1]",xCoded[1])
    newX = np.matrix(xCoded,dtype=float)
    newY = np.matrix(y,dtype=float).T
    theta = (np.linalg.inv(newX.T*newX))*newX.T*newY
   # print(theta)
    return theta

  



  def writeCSV(self, stationID, date, isRedDay, congestions):
    congestions.insert(0,isRedDay)
    congestions.insert(0,date)
    congestions.insert(0,stationID)
 #   print(congestions)
    fileName = stationID+".csv"
    with open(fileName, 'a', newline='') as csvfile:
      fieldNames = ['stationID','date','isRedDay','1','2','3','4','5','6','7','8','9','10','11','12','13','14','15','16','17','18','19','20','21','22','23','24']
      writer = csv.writer(csvfile, delimiter=',')
      writer.writerow(congestions)

  ### Not necessory
  ### To evaluation of hypothesis : Compute cost for linear regression
  ### J : Cost Function - just print J to know cost of hypothesis
  ### X: newX, y: newY, theta : results of getTheta(newX, newY are deleted now in the code ;getTheta)
  def compute_cost(self, X, y, theta):
    newX = np.matrix(X,dtype=float)
    newY = np.matrix(y,dtype=float).T
    #Number of training samples
    m = newY.size
    predictions = newX.dot(theta)
    sqErrors = (predictions - newY)
    J = (1.0 / (2 * m)) * sqErrors.T.dot(sqErrors)
    return J
    
  ### Do Machine Learning!!! ; done when writing csvFile is done
 # date : 0~365
  def congestion(self, stationIDs, stationNames, date):

    #get Current time : 날짜(월/일 0 ~365),시간, 요일/공휴일/등등
  #  date = self.getDate(date)
    #print(stationIDs)
    
    ## do for all stations, and times
    for index in range(len(stationIDs)):
      #make all time of thetas by using stationID&stationName
      weekCongestions = []
      saturCongestions = []
      redCongestions = []
      for wantTime in range(1,25):
        #initialize matrices
        weekDay, weekDayY, saturDay, saturDayY, redDay, redDayY = self.readCSV(stationIDs[index],stationNames[index], wantTime)

        #the first column : x0 = 1
        newWeekDay = self.insertFirstColumn(weekDay)
        newSaturDay = self.insertFirstColumn(saturDay)
        newRedDay = self.insertFirstColumn(redDay)

        #get theta
        thetaWeekDay = self.getTheta(newWeekDay, weekDayY)
        thetaSaturDay = self.getTheta(newSaturDay, saturDayY)
        thetaRedDay = self.getTheta(newRedDay, redDayY)

        weekCongestion = int(thetaWeekDay[0]+thetaWeekDay[1]*date)
        saturCongestion = int(thetaSaturDay[0]+thetaSaturDay[1]*date)
        redCongestion = int(thetaRedDay[0]+thetaRedDay[1]*date)

        if(weekCongestion < 0) : weekCongestion = 0
        if(saturCongestion < 0) : saturCongestion = 0
        if(redCongestion < 0) : redCongestion = 0
        
        #append thetas of week/sat/red order by time(1~24)
        weekCongestions.append(weekCongestion)
        saturCongestions.append(saturCongestion)
        redCongestions.append(redCongestion)
        costFuncWeek = self.compute_cost(newWeekDay,weekDayY,thetaWeekDay)
        costFuncSatur = self.compute_cost(newSaturDay,saturDayY,thetaSaturDay)
        costFuncRed = self.compute_cost(newRedDay,redDayY,thetaRedDay)
        print("stationID: ",stationIDs[index],"time : ",index+1 , "Cost of weekDay: ",costFuncWeek ,"Cost of saturDay: ",costFuncSatur, "Cost of RedDay: ", costFuncRed )
    
      ## this date is 0~364 ; not MM-DD
#      print("stationID: ",stationIDs[index], "date", date)
#      print("weekDay")
#      self.writeCSV(stationIDs[index], date, 0, weekCongestions)
#      print("saturDay")
#      self.writeCSV(stationIDs[index], date, 1, saturCongestions)
#      print("RedDay")
#      self.writeCSV(stationIDs[index], date, 2, redCongestions)
        
    
    #  print(theta[0], theta[1])
    # return theta[0]+theta[1]*date
 #   return weekDayThetas, saturDayThetas, redDayThetas
  






# date : 0 to 365
x = Congestion()




