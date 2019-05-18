//
// Created by GangGongUi on 2016. 7. 14..
//

#include "Logger.h"
#include <android/log.h>


void Logger::logE(const std::string &path, const std::string &text)
{
    const char *buff = text.c_str();
    const char *name = path.c_str();
    __android_log_print(ANDROID_LOG_ERROR, name, buff);
}
