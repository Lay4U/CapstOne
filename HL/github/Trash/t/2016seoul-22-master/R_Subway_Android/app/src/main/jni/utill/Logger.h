//
// Created by GangGongUi on 2016. 7. 14..
//

#ifndef MY_APPLICATION_LOGGER_H
#define MY_APPLICATION_LOGGER_H


#include <iostream>
#include <sstream>




class Logger
{
public:
    void logE(const std::string& path, const std::string& text);

    std::string intToString(int n)
    {

        std::stringstream s;
        s << n;
        return s.str();
    }

};


#endif //MY_APPLICATION_LOGGER_H
