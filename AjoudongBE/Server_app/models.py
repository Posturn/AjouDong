from django.db import models

# Create your models here.


class userAccount(models.Model):
    uID = models.CharField(max_length=20,)
    uPW = models.CharField(max_length=20,)
    uIMG = models.CharField(max_length=128, null=True)
    uName = models.CharField(max_length=10, null=True)
    uJender = models.BooleanField(default=True)
    uSchoolID = models.IntegerField(primary_key=True, null=False, default = 1)
    uMajor = models.CharField(max_length=32, null=True)
    uPhoneNumber = models.IntegerField(null=True)
    uCollege = models.CharField(max_length=32, null=True)

class managerAccount(models.Model):
    mID = models.CharField(max_length=20, primary_key=True)
    mPW = models.CharField(max_length=20, null=True)
    clubID = models.IntegerField()
    clubName = models.CharField(max_length=5,)
    clubIMG = models.CharField(max_length=128,)
    newbieAlarm = models.BooleanField(default=True)

class major_Affiliation(models.Model):
    majorName = models.CharField(max_length=20, primary_key=True)
    majorCollege = models.CharField(max_length=20,)
