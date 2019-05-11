# imageShare
Android Image share demo

Share image code is as below:

    Intent shareIntent = new Intent();
    shareIntent.setAction(Intent.ACTION_SEND);
    shareIntent.putExtra(Intent.EXTRA_STREAM, imageURI);
    shareIntent.setType("image/jpeg");
    startActivity(Intent.createChooser(shareIntent, "Select App"));
    
Check application has permission to read external storae, otherwise sharing can fail on some applications like GMail, Hangout etc.
