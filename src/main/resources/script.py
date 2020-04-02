import pandas as pd
import csv
import sys
#import pathlib
#D:\coding\Idea Projects\JB_Data_Viewer\SacramentocrimeJanuary2006.csv
#dir = pathlib.Path(__file__).parent.absolute()
pd.set_option("display.max_rows", 4, "display.max_columns", None)

path = input()
#chunkSize = int(input())

data = pd.read_csv(path, skipinitialspace=True)
print(data.shape[0] - 1, data.shape[1], sep='\n')
print(*data.columns, sep='\n', flush=True)

while (True):
    fromRow = int(input())
    nRows = int(input())

    data = pd.read_csv(path, skipinitialspace=True, nrows=nRows, skiprows=fromRow+1, header=None)
    print(data.to_csv(index=False, header=False, sep='\n', na_rep='NA'))
