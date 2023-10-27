package srg.resources;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ResourceContainerTest {
    @Test(expected = IllegalArgumentException.class)
    public void constructorNotValidTypeTest() {
        ResourceContainer resourceContainer = new ResourceContainer(ResourceType.FUEL, 0);
    }
    @Test
    public void constructorREPAIRKITTest() {
        ResourceContainer resourceContainer = new ResourceContainer(ResourceType.REPAIR_KIT, 100);
        assertEquals(resourceContainer.getType(), ResourceType.REPAIR_KIT);
        assertEquals(resourceContainer.getAmount(), 100);
    }
    @Test
    public void constructorTRITIUMTest() {
        FuelContainer resourceContainer = new FuelContainer(FuelGrade.TRITIUM, 100);
        assertEquals(resourceContainer.getFuelGrade(), FuelGrade.TRITIUM);
        assertEquals(resourceContainer.getAmount(), 100);
    }
    @Test
    public void constructorHYPERDRIVE_CORETest() {
        FuelContainer resourceContainer = new FuelContainer(FuelGrade.HYPERDRIVE_CORE, 100);
        assertEquals(resourceContainer.getFuelGrade(), FuelGrade.HYPERDRIVE_CORE);
        assertEquals(resourceContainer.getAmount(), 100);
    }
    @Test
    public void canStoreTestREPAIRKIT() {
        ResourceContainer resourceContainer = new ResourceContainer(ResourceType.REPAIR_KIT, 0);
        assertEquals(resourceContainer.canStore(resourceContainer.getType()), true);
    }
    @Test
    public void canStoreTestTRITIUM() {
        FuelContainer resourceContainer = new FuelContainer(FuelGrade.TRITIUM, 0);
        assertEquals(resourceContainer.canStore(resourceContainer.getType()), true);
    }
    @Test
    public void canStoreTestHYPERDRIVECORE() {
        FuelContainer resourceContainer = new FuelContainer(FuelGrade.HYPERDRIVE_CORE, 0);
        assertEquals(resourceContainer.canStore(resourceContainer.getType()), true);
    }
    @Test
    public void getAmountTestREPAIRKIT() {
        ResourceContainer resourceContainer = new ResourceContainer(ResourceType.REPAIR_KIT, 100);
        assertEquals(resourceContainer.getAmount(), 100);
    }
    @Test
    public void getAmountTestTRITIUM() {
        FuelContainer resourceContainer = new FuelContainer(FuelGrade.TRITIUM, 100);
        assertEquals(resourceContainer.getAmount(), 100);
    }
    @Test
    public void getAmountTestHYPERDRIVECORE() {
        FuelContainer resourceContainer = new FuelContainer(FuelGrade.HYPERDRIVE_CORE, 100);
        assertEquals(resourceContainer.getAmount(), 100);
    }
    @Test
    public void setAmountTestREPAIRKIT() {
        ResourceContainer resourceContainer = new ResourceContainer(ResourceType.REPAIR_KIT, 100);
        resourceContainer.setAmount(500);
        assertEquals(resourceContainer.getAmount(), 500);
    }
    @Test
    public void setAmountTestHYPERDRIVECORE() {
        FuelContainer resourceContainer = new FuelContainer(FuelGrade.HYPERDRIVE_CORE, 100);
        resourceContainer.setAmount(500);
        assertEquals(resourceContainer.getAmount(), 500);
    }
    @Test
    public void setAmountTestTRITIUM() {
        FuelContainer resourceContainer = new FuelContainer(FuelGrade.TRITIUM, 100);
        resourceContainer.setAmount(500);
        assertEquals(resourceContainer.getAmount(), 500);
    }
    @Test
    public void toStringTestREPAIRKIT() {
        ResourceContainer resourceContainer = new ResourceContainer(ResourceType.REPAIR_KIT, 100);
        assertEquals(resourceContainer.toString(), "REPAIR_KIT: 100");
    }
    @Test
    public void toStringTestTRITIUM() {
        FuelContainer resourceContainer = new FuelContainer(FuelGrade.TRITIUM, 100);
        assertEquals(resourceContainer.toString(), "FUEL: 100 - TRITIUM");
    }
    @Test
    public void toStringTestHYPERDRIVECORE() {
        FuelContainer resourceContainer = new FuelContainer(FuelGrade.HYPERDRIVE_CORE, 100);
        assertEquals(resourceContainer.toString(), "FUEL: 100 - HYPERDRIVE_CORE");
    }
    @Test
    public void getShortNameTestREPAIRKIT() {
        ResourceContainer resourceContainer = new ResourceContainer(ResourceType.REPAIR_KIT, 100);
        assertEquals(resourceContainer.getShortName(), "REPAIR_KIT");
    }
    @Test
    public void getShortNameTestTRITIUM() {
        FuelContainer resourceContainer = new FuelContainer(FuelGrade.TRITIUM, 100);
        assertEquals(resourceContainer.getShortName(), "TRITIUM");
    }
    @Test
    public void getShortNameTestHYPERDRIVECORE() {
        FuelContainer resourceContainer = new FuelContainer(FuelGrade.HYPERDRIVE_CORE, 100);
        assertEquals(resourceContainer.getShortName(), "HYPERDRIVE_CORE");
    }

}
