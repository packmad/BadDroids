package it.unige.dibris.baddroids.engine;


import java.io.File;
import java.util.Set;

import it.unige.dibris.baddroids.domain.MethodInvocation;

/**
 * Created by Simone Aonzo on 13/03/17.
 */
public abstract class InvocationExtractor {
    public abstract Set<MethodInvocation> extractApiInvocations(File apk);
}
