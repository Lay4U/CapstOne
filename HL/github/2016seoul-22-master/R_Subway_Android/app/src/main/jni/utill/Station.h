//
// Created by GangGongUi on 2016. 7. 21..
//

#ifndef R_SUBWAY_ANDROID_STATION_H
#define R_SUBWAY_ANDROID_STATION_H


#include <string>


class Station
{

public:
    int index;
    Station() { };
    Station(int index) : index(index) { };

    bool isTransfar() const {
        return transfar;
    }

    void setTransfar(bool transfar) {
        Station::transfar = transfar;
    }

    int getLaneType() const {
        return laneType;
    }

    void setLaneType(int laneType) {
        Station::laneType = laneType;
    }

    const std::string &getStationName() const {
        return stationName;
    }

    void setStationName(const std::string &stationName) {
        Station::stationName = stationName;
    }

    bool operator <(const Station& S) const {
            return false;
    }

private:
    bool transfar;
    int laneType;
    std::string stationName;

};


#endif //R_SUBWAY_ANDROID_STATION_H
