import pandas as pd
import csv
import sys

#data = pd.read_csv(path, skipinitialspace=True)
#print(data.shape[0] - 1, data.shape[1], sep='\n')
#print(*data.columns, sep='\n', flush=True)

#path = "D:\coding\Idea Projects\JB_Data_Viewer\src\\test\\java\\sample.csv"
path = input()

try:
    header = pd.read_csv(path, skipinitialspace=True, nrows=0)
    file_found = True

except FileNotFoundError:
    file_found = False

# Print whether file is found
print(file_found, flush=True)
if not file_found:
    exit(0)

# Print number of columns
print(header.shape[1])
# Print column names
print(*header.columns, sep='\n', flush=True)


iter = pd.read_csv(path, skipinitialspace=True, iterator=True)

while (True):
    # How many rows to read
    chunkSize = int(input())

    try:
        data = iter.get_chunk(chunkSize)
        # Print number of rows loaded
        print(data.shape[0])
        # Print loaded rows
        print(data.to_csv(index=False, header=False, sep='\n', na_rep='NA'), flush=True, end='')

    except StopIteration:
        # No rows left
        print(0, flush=True)
