from django.db import models

# Create your models here.
class Ads(models.Model):
    adsID = models.IntegerField(auto_created=True, primary_key=True)
    advertiserID = models.CharField(max_length=32,)
    adsSpace = models.IntegerField()
    adsIMG = models.CharField(max_length=128)
    adsView = models.IntegerField()
    adsrestViews = models.IntegerField()

class AppliedClubList(models.Model):
    clubID = models.ForeignKey('Club', on_delete=models.CASCADE,)
    applyID = models.IntegerField(auto_created=True, primary_key=True)
    memberState = models.BooleanField()

class Apply(models.Model):
    clubID = models.ForeignKey('Club', on_delete=models.CASCADE,)
    uSchoolID = models.ForeignKey('UserAccount', on_delete=models.CASCADE,)
    additionalApplyContent = models.CharField(max_length= 1024)

class ClubMember(models.Model):
    clubID = models.ForeignKey('Club', on_delete=models.CASCADE,)
    uSchoolID = models.ForeignKey('UserAccount', on_delete=models.CASCADE,)

class ClubStatistic(models.Model):
    clubID = models.ForeignKey('Club', on_delete=models.CASCADE,)
    memberNumber = models.IntegerField()
    menNumber = models.IntegerField()
    womenNumber = models.IntegerField()
    overRatio12 = models.FloatField()
    Ratio13 = models.FloatField()
    Ratio14 = models.FloatField()
    Ratio15 = models.FloatField()
    Ratio16 = models.FloatField()
    Ratio17 = models.FloatField()
    Ratio18 = models.FloatField()
    Ratio19 = models.FloatField()
    engineeringRatio = models.FloatField()
    ITRatio = models.FloatField()
    naturalscienceRatio = models.FloatField()
    managementRatio = models.FloatField()
    humanitiesRatio = models.FloatField()
    socialscienceRatio = models.FloatField()
    nurseRatio = models.FloatField()


class Event(models.Model):
    eventID = models.IntegerField(auto_created=True, primary_key= True)
    eventName = models.CharField(max_length=32)
    eventDate = models.DateTimeField()
    clubID = models.ForeignKey('Club', on_delete=models.CASCADE,)
    eventInfo = models.CharField(max_length=1024)
    eventFAQ = models.CharField(max_length=1024)

class EventIMG(models.Model):
    eventIMGKey = models.IntegerField(auto_created=True, primary_key=True)
    eventIMG = models.CharField(max_length=128)
    eventID = models.ForeignKey('Event', on_delete=models.CASCADE)

class markedClubList(models.Model):
    clubID = models.ForeignKey('Club', on_delete=models.CASCADE,)
    uSchoolID = models.ForeignKey('UserAccount', on_delete=models.CASCADE,)

class Tag(models.Model):
    clubTag = models.CharField(max_length=64)

class TaggedClubList(models.Model):
    clubID = models.ForeignKey('Club', on_delete=models.CASCADE,)
    uSchoolID = models.ForeignKey('UserAccount', on_delete=models.CASCADE,)

class UserAlarm(models.Model):
    uSchoolID = models.ForeignKey('UserAccount', on_delete=models.CASCADE,)
    newclubAlarm = models.BooleanField()
    stateAlarm = models.BooleanField()
    eventAlarm = models.BooleanField()
    autoLogin = models.BooleanField()
    unreadEvent = models.BooleanField()

class UserAccount(models.Model):
    uID = models.CharField(max_length=20,)
    uPW = models.CharField(max_length=20,)
    uIMG = models.CharField(max_length=128, null=True)
    uName = models.CharField(max_length=10, null=True)
    uJender = models.BooleanField(default=True)
    uSchoolID = models.IntegerField(primary_key=True, null=False, default = 1)
    uMajor = models.CharField(max_length=32, null=True)
    uPhoneNumber = models.IntegerField(null=True)
    uCollege = models.CharField(max_length=32, null=True)

class ManagerAccount(models.Model):
    mID = models.CharField(max_length=20, primary_key=True)
    mPW = models.CharField(max_length=20, null=True)
    clubID = models.ForeignKey('Club', on_delete=models.CASCADE,)
    clubName = models.CharField(max_length=5,)
    clubIMG = models.CharField(max_length=128,)
    newbieAlarm = models.BooleanField(default=True)

class Club(models.Model):
     clubID = models.IntegerField(primary_key=True)
     clubName = models.CharField(max_length=32)
     clubCategory = models.CharField(max_length=256)
     clubIMG = models.CharField(max_length=128)
     clubMajor = models.IntegerField(default=1)
     clubDues = models.FloatField(max_length=3)


class ClubPromotion(models.Model):
    clubID = models.ForeignKey('Club', on_delete=models.CASCADE,)
    promotionID = models.IntegerField(primary_key=True)
    posterIMG = models.CharField(max_length=128)
    clubInfo = models.CharField(max_length=4096)
    clubApply = models.CharField(max_length=1024)
    clubFAQ = models.CharField(max_length=2048)
    clubContact = models.CharField(max_length=1024)
    additionalApply = models.CharField(max_length=1024)
    recruitStartDate = models.DateField()
    recruitEndDate = models.DateField()

class ClubActivity(models.Model):
    clubID = models.ForeignKey('Club', on_delete=models.CASCADE,)
    clubActivityFile = models.CharField(max_length=128)
    clubActivityInfo = models.CharField(max_length=256)
    clubActivityID = models.IntegerField(primary_key=True)
    clubActivityDetail = models.CharField(max_length=2048, null=True)

class Major_Affiliation(models.Model):
    majorName = models.CharField(max_length=20, primary_key=True)
    majorCollege = models.CharField(max_length=20,)
