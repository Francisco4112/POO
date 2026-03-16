package com.grupo06.user;

/**
 * Enum representing the types of subscription plans available.
 */
public enum SubscriptionPlanType {
    /**
     * Free subscription plan.
     */
    FREE("Free plan", Free.class),

    /**
     * Base premium subscription plan.
     */
    PREMIUM_BASE("Premium base plan", PremiumBase.class),

    /**
     * Top-tier premium subscription plan.
     */
    PREMIUM_TOP("Premium top plan", PremiumTop.class);

    private final String displayName;
    private final Class<? extends SubscriptionPlan> planClass;

    /**
     * Constructs a SubscriptionPlanType with a display name and associated class.
     *
     * @param displayName the user-friendly name of the plan
     * @param planClass   the class implementing the subscription plan
     */
    SubscriptionPlanType(String displayName, Class<? extends SubscriptionPlan> planClass) {
        this.displayName = displayName;
        this.planClass = planClass;
    }

    /**
     * Creates a new instance of the associated subscription plan class.
     *
     * @return a new SubscriptionPlan instance
     * @throws RuntimeException if instantiation fails
     */
    public SubscriptionPlan createInstance() {
        try {
            return planClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Falha ao criar plano: " + displayName, e);
        }
    }

    /**
     * Returns the display name of the subscription plan.
     *
     * @return the display name
     */
    public String getDisplayName() {
        return displayName;
    }
}
