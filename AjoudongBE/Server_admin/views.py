from django.shortcuts import render, redirect

# Create your views here.
def login(request):
    nlist = [1, 3, 5, 7, 9, 8, 6, 4, 2]
    return render(request,'adminLogin.html', {'num1':2, 'hello': "안녕하세요", 'number': nlist})

def management(request):
    if request.method == "POST":
        aID = request.POST["id"]
        aPW = request.POST["pw"]
        if aID == "hello":
            if aPW == "world":
                return render(request,'clubManagement.html')
    else:
        return redirect('login')
    return redirect('login')

def advertisement(request):
    if request.method == "POST":
        return render(request,'ajoudongAds.html')
    else:
        return render(request,'ajoudongAds.html')