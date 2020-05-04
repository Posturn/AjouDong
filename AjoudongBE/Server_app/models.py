from django.db import models

# Create your models here.


class userAccount(models.Model):
    # name = models.CharField(max_length=200)
    # mail = models.CharField(max_length=200)
    # age = models.IntegerField(default=0)
    uID = models.CharField(max_length=20)
    uPW = models.IntegerField()
    uIMG = models.CharField(max_length=128)
    uName = models.CharField(max_length=10)
    uJender = models.BooleanField()
    uSchoolID = models.IntegerField(private_key=True)#private key
    uMajor = models.CharField(max_length=32)
    uPhoneNumber = models.IntegerField()
    uCollege = models.CharField(max_length=32)

class managerAccount(models.Model):
    mID = models.CharField(max_length=20, private_key=True)#private key
    clubID = models.IntegerField()
    clubName = models.CharField(5)
    clubIMG = models.CharField(128)
    newbieAlarm = models.BooleanField()
