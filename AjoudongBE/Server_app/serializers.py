from rest_framework import serializers, viewsets

from Server_app.models import *



class UserAccountSerializer(serializers.ModelSerializer):
    class Meta:
        model = UserAccount
        fields = ('uID', 'uPW', 'uIMG', 'uName', 'uJender', 'uSchoolID', 'uMajor', 'uPhoneNumber', 'uCollege')

class ManagerAccountSerializer(serializers.ModelSerializer):
    class Meta:
        model = ManagerAccount
        fields = ('mID', 'mPW', 'clubName', 'clubIMG', 'newbieAlarm', 'clubID')

class clubPromotionSerializer(serializers.ModelSerializer):
    class Meta:
        model = ClubPromotion
        fields = ('posterIMG', 'clubInfo', 'clubFAQ', 'clubApply', 'clubContact', 'additionalApply', 'recruitStartDate', 'recruitEndDate')

class clubActivitySerializer(serializers.ModelSerializer):
    class Meta:
        model = ClubActivity
        fields = ('clubActivityFile', 'clubActivityInfo', 'clubActivityID','clubID','clubActivityDetail')
        
class MajorSerializer(serializers.ModelSerializer):
    class Meta:
        model = Major_Affiliation
        fields=('majorName', 'majorCollege', )

class ClubSerializer(serializers.ModelSerializer):
    class Meta:
        model = Club
        fields=('clubID','clubName','clubCategory','clubIMG','clubMajor','clubDues')

class BookmarkSerializer(serializers.ModelSerializer):
    class Meta:
        model = MarkedClubList
        fields=('clubID', 'uSchoolID',)

class UserInfoSerializer(serializers.ModelSerializer):
    class Meta:
        model = UserAccount
        fields=('uID', 'uPW', 'uIMG','uName','uJender','uSchoolID','uMajor','uPhoneNumber','uCollege')

class ManagerFilterSerializer(serializers.ModelSerializer):
    class Meta:
        model = TaggedClubList
        fields=('clubID', 'id', 'clubTag')

class TagSerializer(serializers.ModelSerializer):
    class Meta:
        model = Tag
        fields=('clubTag',)

class ClubStatisticSerializer(serializers.ModelSerializer):
    class Meta:
        model = ClubStatistic
        fields=('clubID', 'memberNumber' ,'menNumber', 'womenNumber', 'overRatio12', 'Ratio13', 'Ratio14',
    'Ratio15', 'Ratio16', 'Ratio17', 'Ratio18', 'Ratio19', 'engineeringRatio', 'ITRatio', 'naturalscienceRatio',
    'managementRatio', 'humanitiesRatio', 'socialscienceRatio', 'nurseRatio')

class ClubQuestionSerializer(serializers.ModelSerializer):
    class Meta:
        model = ClubPromotion
        fields=('additionalApply',)

class ClubRecruitSerializer(serializers.ModelSerializer):
    class Meta:
        model = TaggedClubList
        fields=('clubID',)

class ClubMemberSerializer(serializers.ModelSerializer):
    class Meta:
        model = ClubMember
        fields=('id', 'clubID', 'uSchoolID',)

class EventSerializer(serializers.ModelSerializer):
    class Meta:
        model = Event
        fields=('eventID','eventName','eventDate','eventInfo','eventIMG','clubID',)

class ClubAlarmSerializer(serializers.ModelSerializer):
    class Meta:
        model = UserAlarm
        fields = ('stateAlarm','eventAlarm','newclubAlarm','unreadEvent',)