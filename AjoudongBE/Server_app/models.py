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

class club(models.Model):
     clubID = models.IntegerField(primary_key=True)
     clubName = models.CharField(max_length=32)
     clubCategory = models.CharField(max_length=256)
     clubIMG = models.CharField(max_length=128)
     clubMajor = models.IntegerField(default=1)
     clubDues = models.FloatField(max_length=3)


class clubPromotion(models.Model):
    clubID = models.ForeignKey('club', on_delete=models.CASCADE,)
    promotionID = models.IntegerField(primary_key=True)
    posterIMG = models.CharField(max_length=128)
    clubInfo = models.CharField(max_length=4096)
    clubApply = models.CharField(max_length=1024)
    clubFAQ = models.CharField(max_length=2048)
    clubContact = models.CharField(max_length=1024)
    additionalApply = models.CharField(max_length=1024)
    recruitStartDate = models.DateField()
    recruitEndDate = models.DateField()

class clubActivity(models.Model):
    clubID = models.ForeignKey('club', on_delete=models.CASCADE,)
    clubActivityFile = models.CharField(max_length=128)
    clubActivityInfo = models.CharField(max_length=256)
    clubActivityID = models.IntegerField(primary_key=True)

class major_Affiliation(models.Model):
    majorName = models.CharField(max_length=20, primary_key=True)

