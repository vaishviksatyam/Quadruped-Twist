with open('front.txt', 'r') as file :
  filedata = file.read()


# Replace the target string
filedata = filedata.replace('255,255', '{(byte)255,255')

# Replace the target string
filedata = filedata.replace(',', ',(byte)')

# Replace the target string
filedata = filedata.replace('\n', '},')


# Write the file out again
with open('front2.txt', 'w') as file:
  file.write(filedata)
