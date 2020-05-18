

from django.db import migrations, models
import django.db.models.deletion


class Migration(migrations.Migration):

    initial = True

    dependencies = [
    ]

    operations = [
        migrations.CreateModel(
            name='club',
            fields=[
                ('clubID', models.IntegerField(primary_key=True, serialize=False)),
                ('clubName', models.CharField(max_length=32)),
                ('clubCategory', models.CharField(max_length=256)),
                ('clubIMG', models.CharField(max_length=128)),
                ('clubMajor', models.IntegerField(default=1)),
                ('clubDues', models.FloatField(max_length=3)),
            ],
        ),
        migrations.CreateModel(
            name='managerAccount',
            fields=[
                ('mID', models.CharField(max_length=20, primary_key=True, serialize=False)),
                ('mPW', models.CharField(max_length=20, null=True)),
                ('clubID', models.IntegerField()),
                ('clubName', models.CharField(max_length=5)),
                ('clubIMG', models.CharField(max_length=128)),
                ('newbieAlarm', models.BooleanField(default=True)),
            ],
        ),
        migrations.CreateModel(
            name='userAccount',
            fields=[
                ('uID', models.CharField(max_length=20)),
                ('uPW', models.CharField(max_length=20)),
                ('uIMG', models.CharField(max_length=128, null=True)),
                ('uName', models.CharField(max_length=10, null=True)),
                ('uJender', models.BooleanField(default=True)),
                ('uSchoolID', models.IntegerField(default=1, primary_key=True, serialize=False)),
                ('uMajor', models.CharField(max_length=32, null=True)),
                ('uPhoneNumber', models.IntegerField(null=True)),
                ('uCollege', models.CharField(max_length=32, null=True)),
            ],
        ),
        migrations.CreateModel(
            name='clubPromotion',
            fields=[
                ('promotionID', models.IntegerField(primary_key=True, serialize=False)),
                ('posterIMG', models.CharField(max_length=128)),
                ('clubInfo', models.CharField(max_length=4096)),
                ('clubApply', models.CharField(max_length=1024)),
                ('clubFAQ', models.CharField(max_length=2048)),
                ('clubContact', models.CharField(max_length=1024)),
                ('additionalApply', models.CharField(max_length=1024)),
                ('recruitStartDate', models.DateField()),
                ('recruitEndDate', models.DateField()),
                ('clubID', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='Server_app.club')),
            ],
        ),
        migrations.CreateModel(
            name='clubActivity',
            fields=[
                ('clubActivityFile', models.CharField(max_length=128)),
                ('clubActivityInfo', models.CharField(max_length=256)),
                ('clubActivityID', models.IntegerField(primary_key=True, serialize=False)),
                ('clubID', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='Server_app.club')),
            ],
        ),
    ]
