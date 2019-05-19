//
// Created by GangGongUi on 2016. 7. 6..
//

#ifndef RSUBWAY_MAIN_BITQUEUE_H_
#define RSUBWAY_MAIN_BITQUEUE_H_

#include <cstdio>
#include <utility>
#include <stl/_algobase.h>


template <typename E>
class bQueue
{
private:
    int MAX_SIZE = 0;
    int front = 0, rear = 0;
    E *arr;

    void queueFull()
    {
        E *copyArr = new E[MAX_SIZE * 2];

        int start = (front + 1) % MAX_SIZE;

        if(start < 2)
        {
            std::copy(arr + start, arr + start + MAX_SIZE - 1, copyArr);
        }
        else
        {
            std::copy(arr + start, arr + MAX_SIZE, copyArr);
            std::copy(arr, arr + rear + 1, copyArr + MAX_SIZE - start);
        }
        front = 2 * MAX_SIZE -1;
        rear = MAX_SIZE - 2;
        MAX_SIZE *= 2;

        arr = copyArr;
    }

public:
    bQueue(int MAX_SIZE = 10) : MAX_SIZE(MAX_SIZE)
    {
        arr = new E[MAX_SIZE];
    };

    void push(const E& newValue)
    {
        rear = (rear + 1) % MAX_SIZE;
        if(front == rear)
            queueFull();
        arr[rear] = newValue;
    }

    const E top()
    {
        if(isEmpty())
            return E();
        return arr[(front + 1) % MAX_SIZE];
    }

    void pop()
    {
        if(isEmpty())
            return;
        front = (front + 1) % MAX_SIZE;
    }

    bool isEmpty()
    {
        return front == rear;
    }
};

template <typename E>
class BitQueue
{
    const int MAX_SIZE;
    const int MAX_64 = 4096;
    int MASK_SIZE;
    long long MASK_TABLE;

public:
    BitQueue(int max_size) : MAX_SIZE(max_size), MASK_TABLE(0LL)
    {
        MASK_SIZE = ((MAX_SIZE + 63) >> 6);
        table = new bQueue<std::pair<E, int> >[MAX_SIZE + 1];
        mask = new long long[MASK_SIZE + 1];
        MASK_TABLE = 0LL;
        for(int i = 0; i < MASK_SIZE; i++)
        {
            mask[i] = 0LL;
        }
        size_ = 0;
    };

    virtual ~BitQueue<E>()
    {
        delete [] mask;
    }

    void push(const std::pair<E ,int> &newValue);
    std::pair<E, int> top();
    void pop();
    int size() const;
    bool empty() const;
    void clear();

private:
    bQueue<std::pair<E, int> > *table;
    long long *mask;
    int memo = 0;
    int size_ = 0;

    void onMaskBit(int idx)
    {
        int offset = (idx >> 6);

        if(memo > offset)
            memo = offset;

        mask[offset] |= (1LL << (idx % 64));

#if MAX_SIZE < MAX_64
        MASK_TABLE |= (1LL << offset);
#endif
    }

    void offMaskBit(int idx)
    {
        int offset = (idx >> 6);
        if (mask[offset] == 0LL && MASK_SIZE > offset)
            memo = offset+1;

        mask[offset] &= ~(1LL << (idx % 64));
        if(MAX_SIZE  < MAX_64 && mask[offset] == 0LL)
            MASK_TABLE &= ~(1LL << offset);
    }

    bool isMaskOn(int idx) const
    {
        return mask[idx >> 6] & (1LL << (idx % 64));
    }
};

template <typename E>
void BitQueue<E>::push(const std::pair<E, int> &newValue)
{
    int idx = newValue.second;

    if(!isMaskOn(idx))
        onMaskBit(idx);

    table[idx].push(newValue);
    size_++;
}



template <typename E>
std::pair<E, int> BitQueue<E>::top()
{
    for(int i = memo, idx = 0; i < MASK_SIZE; i++)
    {
#if MAX_SIZE < MAX_64
        i = __builtin_ctzll(MASK_TABLE);
        idx = __builtin_ctzll(mask[i]);
#else
        idx = __builtin_ctzll(mask[i]);
#endif
        if ((idx < 64) && mask[i] != 0LL)
        {
            int offset = (64 * i) + idx;
            memo = i;
            return table[offset].top();
        }
    }



    return table[0].top();
}

template <typename E>
void BitQueue<E>::pop()
{
    if(size_ == 0)
        return;

    size_--;

    for(int i = memo, idx = 0; i < MASK_SIZE; i++)
    {
#if MAX_SIZE < MAX_64
        i = __builtin_ctzll(MASK_TABLE);
        idx = __builtin_ctzll(mask[i]);
#else
        idx = __builtin_ctzll(mask[i]);
#endif
        if((idx < 64) && mask[i] != 0LL)
        {
            int offset = (64 * i) + idx;
            memo = i;
            table[offset].pop();
            if(table[offset].isEmpty())
            {
                offMaskBit(offset);
            }

            break;
        }
    }
}

template <typename E>
int BitQueue<E>::size() const
{
    return size_;
}

template <typename E>
bool BitQueue<E>::empty() const
{
    return size_ == 0;
}

template <typename E>
void BitQueue<E>::clear()
{
    delete table;
    table = new bQueue<E>[MAX_SIZE + 1];
    size_ = 0;
    MASK_TABLE = 0LL;
    for(int i = 0; i < MASK_SIZE; i++)
        mask[i] = 0LL;
}

#endif //RSUBWAY_MAIN_BITQUEUE_H_
