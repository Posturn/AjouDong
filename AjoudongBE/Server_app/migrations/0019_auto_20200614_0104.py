# Generated by Django 3.0.4 on 2020-06-13 16:04

from django.db import migrations, models
import django.db.models.deletion


class Migration(migrations.Migration):

    dependencies = [
        ('Server_app', '0018_auto_20200614_0051'),
    ]

    operations = [
        migrations.AlterField(
            model_name='event',
            name='eventID',
            field=models.AutoField(primary_key=True, serialize=False),
        ),
    ]
