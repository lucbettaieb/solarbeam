using System;
using System.Collections.Generic;
using Pololu.Usc;
using Pololu.UsbWrapper;


public class SolarBeam_Pololu
{
    private static Usc usc = null;

    public static void TryToReconnect()
    {
        if (usc != null)
            return;

        foreach (DeviceListItem d in Usc.getConnectedDevices())
        {
            // if (d.serialNumber.Equals("00012345")) { continue; }
            usc = new Usc(d);
            return;
        }
    }

    public static void TryToDisconnect()
    {
        if (usc == null)
        {
            return;
        }

        try
        {
            usc.Dispose();
        }
        catch (Exception e)
        {
        }
        finally
        {
            usc = null;
        }
    }

    public static void SetTarget(byte servo, ushort value)
    {
        usc.setTarget(servo, value);
    }
}

