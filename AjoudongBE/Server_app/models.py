from django.db import models

# Create your models here.


class userAccount(models.Model):
    # name = models.CharField(max_length=200)
    # mail = models.CharField(max_length=200)
    # age = models.IntegerField(default=0)
    uID = models.CharField(max_length=20)
    uPW = models.IntegerField()
