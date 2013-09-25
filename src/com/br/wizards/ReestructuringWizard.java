package com.br.wizards;

import java.io.IOException;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.common.util.EList;
import org.eclipse.gmt.modisco.java.Model;
import org.eclipse.gmt.modisco.omg.kdm.code.AbstractCodeElement;
import org.eclipse.gmt.modisco.omg.kdm.code.ClassUnit;
import org.eclipse.gmt.modisco.omg.kdm.code.CodeElement;
import org.eclipse.gmt.modisco.omg.kdm.code.CodeFactory;
import org.eclipse.gmt.modisco.omg.kdm.code.CodeModel;
import org.eclipse.gmt.modisco.omg.kdm.code.FloatType;
import org.eclipse.gmt.modisco.omg.kdm.code.IntegerType;
import org.eclipse.gmt.modisco.omg.kdm.code.Package;
import org.eclipse.gmt.modisco.omg.kdm.code.StorableKind;
import org.eclipse.gmt.modisco.omg.kdm.code.StorableUnit;
import org.eclipse.gmt.modisco.omg.kdm.core.KDMEntity;
import org.eclipse.gmt.modisco.omg.kdm.kdm.Attribute;
import org.eclipse.gmt.modisco.omg.kdm.kdm.KDMModel;
import org.eclipse.gmt.modisco.omg.kdm.kdm.KdmFactory;
import org.eclipse.gmt.modisco.omg.kdm.kdm.KdmPackage;
import org.eclipse.gmt.modisco.omg.kdm.kdm.Segment;
import org.eclipse.gmt.modisco.omg.kdm.source.InventoryModel;
import org.eclipse.gmt.modisco.omg.kdm.source.SourceFactory;
import org.eclipse.gmt.modisco.omg.kdm.source.SourceFile;
import org.eclipse.gmt.modisco.omg.kdm.source.SourceRef;
import org.eclipse.gmt.modisco.omg.kdm.source.SourceRegion;
import org.eclipse.jface.wizard.Wizard;

import com.br.databaseDDL.DataBase;
import com.br.util.models.UtilJavaModel;
import com.br.util.models.UtilKDMModel;
import com.br.utils.CreateCommentOnJavaModelBasedInSqlStatement;
import com.br.utils.CreateUMLModelBasedOnKDMDATA;
import com.br.utils.CreateUMLModelBasedOnKDMModel;
import com.br.utils.ProjectSelectedToModernize;
import com.br.utils.ReestructuringToBeRealized;
import com.br.utils.modernization.kdm.DAO.ModernizationKDMToDAO;

public class ReestructuringWizard extends Wizard {

	
	protected ReestructuringPageWizard page;
	
	private ClassUnit StringToBeUsed = null;
	
	public ReestructuringWizard() {
		super();
		setWindowTitle("Modernization");
		setNeedsProgressMonitor(true);
		
	}

	@Override
	public void addPages() {
		
		this.page = new ReestructuringPageWizard();
		addPage(page);
		
	}

	@Override
	public boolean performFinish() {
		
		
		System.out.println("DAO " +  page.getBtnRadioButton().getSelection());
		
		System.out.println("JPA " +  page.getBtnModernizeToJpa().getSelection());
		
		System.out.println("RESTFULL " +  page.getBtnModernizeToRestufull().getSelection());
		
		System.out.println(" O Ckeck box getEnable " + page.getBtnCheckButton().getEnabled());
		
		System.out.println(" O Ckeck box is visible " + page.getBtnCheckButton().isVisible());
		
		System.out.println(" O Ckeck box isEnable " + page.getBtnCheckButton().isEnabled());
		
		System.out.println(" O Ckeck box is selected " + page.getBtnCheckButton().getSelection());
		
		this.setChoseModernization();
		
		
		
		
		System.out.println("DAO " + ReestructuringToBeRealized.modernizeToDAO);
		
		System.out.println("JPA " + ReestructuringToBeRealized.modernizeToJPA);
		
		System.out.println("RESt " + ReestructuringToBeRealized.modernizeToRESTFULL);
		
		System.out.println("REStMobile " + ReestructuringToBeRealized.modernizeToRESTFULLMobile);
		
		
		
		Job job = new Job("Modernizing the Project: " + ProjectSelectedToModernize.projectSelected.getElementName()) {
			  @Override
			  protected IStatus run(IProgressMonitor monitor) {
				// Set total number of work units
					monitor.beginTask("Modernizing", 1000);
	 
					for (int i = 0; i < 1000; i++) {
						try {
							Thread.sleep(1000);
							
							if (i < 150) {
								
								monitor.subTask("Getting all information" + i);
								
							} else if ( ( i > 150 ) && (i < 200) ) {
								
								monitor.subTask("Reading the KDM information" + i);
								
							} else if ( ( i > 200 ) && (i < 230) ) {
								
								monitor.subTask("Starting the algorithm" + i);
								
							} else if ( ( i > 230 ) && (i < 300) ) {
								
								monitor.subTask("Creating all entities" + i);
								
							} else if ( ( i > 300 ) && (i < 350) ) {
								
								monitor.subTask("Creating all Packages" + i);
								
							} else if ( ( i > 350 ) && (i < 453) ) {
								
								monitor.subTask("Creating all ClassUnits" + i);
								
							} else if ( ( i > 453 ) && (i < 550) ) {
								
								monitor.subTask("Creating all MethodsUnits and RelationShips" + i);
								
	
							} else {
								
								monitor.subTask("Performing the algorithm" + i);
								
							}
							
							
							// Report that 10 units are done
							monitor.worked(1);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
					}
					return Status.OK_STATUS;
				}
			};

			// Start the Job
			job.schedule();
			
			
			DataBase mey = DataBase.getInstance(null);
			
			IResource resrouce = ProjectSelectedToModernize.projectSelected.getProject().findMember("/MODELS_PIM/KDMMODEL.xmi");
			
			IFile fileToBeReadKDM = (IFile) resrouce;
			
			ModernizationKDMToDAO modernizationKDM2DAO = new ModernizationKDMToDAO();
			
			Segment segmentNew = modernizationKDM2DAO.start(fileToBeReadKDM.getLocationURI().toString(), mey);
			
			UtilKDMModel testeKDM = new UtilKDMModel();
			
			testeKDM.save(segmentNew);
			
			try {
				ResourcesPlugin.getWorkspace().getRoot().getProject(ProjectSelectedToModernize.projectSelected.getProject().getName()).refreshLocal(IResource.DEPTH_INFINITE, null);
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
//			createAlunoNoKDM();
			
			CreateUMLModelBasedOnKDMDATA.createModel();
			
			
			this.generateNewJavaModelWithComment();
			
			
			System.out.println("Criou os dois modelos");
		
			CreateUMLModelBasedOnKDMModel.createModel();
			
//			DataBase mey = DataBase.getInstance(null);
			
			System.out.println("O DataBase obtido pelo singleton foi " + mey.getDataBaseName());
			
			System.out.println("O DataBase obtido pelo singleton foi " + mey.getDataBaseTables());
			
			
			
		return true;
	}
	
	
	
	private void createAlunoNoKDM () {
		
		UtilKDMModel testeKDM = new UtilKDMModel();
		
		
		IResource resrouce = ProjectSelectedToModernize.projectSelected.getProject().findMember("/MODELS_PIM/KDMMODEL.xmi");
		
		IFile fileToBeRead = (IFile) resrouce;
		
		Segment segment = testeKDM.load(fileToBeRead.getLocationURI().toString());
		
		List<KDMModel> models = segment.getModel();
		
		for (KDMModel kdmModel : models) {
			System.out.println("O nome dos segmentos s‹o " + kdmModel.getName());
		}
		
		CodeModel codeModel = (CodeModel) models.get(0);
		
		Package packageKDM = (Package) codeModel.getCodeElement().get(0);
		
//		ClassUnit classUnit = (ClassUnit) packageKDM.getCodeElement().get(0);
//		
//		System.out.println("O nome da classe Ž " + classUnit.getName());
//		
//		System.out.println("A class Ž abstract? " + classUnit.getIsAbstract());
//		
//		System.out.println("A class Ž abstract? " + classUnit.getIsAbstract());
		
		CodeModel codeModelExternals = (CodeModel) models.get(1);
		
		this.StringToBeUsed = this.getStringType(codeModelExternals);
		
		this.criarClassUNITAluno(packageKDM);
		
		
		InventoryModel invent = (InventoryModel) models.get(2);
		
		invent.getInventoryElement().add(this.criarSourceFile());
		
		
		
		
		
		
		testeKDM.save(segment);
		
		try {
			ResourcesPlugin.getWorkspace().getRoot().getProject(ProjectSelectedToModernize.projectSelected.getProject().getName()).refreshLocal(IResource.DEPTH_INFINITE, null);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
			
		
		
	}
	
	
	private Attribute criarAttribute () {
		
		
		Attribute attibute = KdmFactory.eINSTANCE.createAttribute();
		
		attibute.setTag("export");
		
		attibute.setValue("public");
		
		return attibute;
		
	}
	
	
	
	private SourceFile criarSourceFile () {
		
		SourceFile sourceFile = SourceFactory.eINSTANCE.createSourceFile();
		
		sourceFile.setName("Aluno.java");
		
		sourceFile.setPath("/Users/rafaeldurelli/Documents/runtime-EclipseApplication/Legacy_System_To_Test/src/Aluno.java");
		
		sourceFile.setLanguage("java");
		
		
		return sourceFile;
		
	}
	
	private SourceRegion criarSourceRegion () {
		
		SourceRegion sourceRegion = SourceFactory.eINSTANCE.createSourceRegion();
		
		sourceRegion.setLanguage("java");
		
		sourceRegion.setFile(this.criarSourceFile());
		
		return sourceRegion;
		
		
		
	}
	
	private SourceRef criarSource ( ) {
		
		SourceRef sourceRef = SourceFactory.eINSTANCE.createSourceRef();
		
		sourceRef.setLanguage("java");
		
		sourceRef.getRegion().add(this.criarSourceRegion());
		
		return sourceRef;
		
	}
	
	private void criarClassUNITAluno (Package packageKDM) {
		
		
		
		
		
		ClassUnit classUnitALUNO = CodeFactory.eINSTANCE.createClassUnit();
		
		classUnitALUNO.setIsAbstract(false);
		
		classUnitALUNO.setName("Aluno");
		
	
		
		classUnitALUNO.getAttribute().add(criarAttribute());
		
		
//		classUnitALUNO.getSource()
		
		classUnitALUNO.getSource().add(this.criarSource());
		
		
		String[] attributes = {"RA", "id", "lastName", "name"};
		
		for (int i = 0; i < attributes.length; i++) {
		
			this.criarAttibutoName(classUnitALUNO, attributes[i]);
			
		}
		
		
		
		packageKDM.getCodeElement().add(classUnitALUNO);
		
		
		
		
	}
	
	private void criarAttibutoName(ClassUnit classUnit, String name) {
		
		StorableUnit storableUnit = CodeFactory.eINSTANCE.createStorableUnit();
		
		storableUnit.setName(name);
		
		storableUnit.setKind(StorableKind.GLOBAL);
		
		storableUnit.getAttribute().add(this.criarAttibuteForName());
		
//		storableUnit.getSource().add(classUnit.getSource().get(0));
		
		storableUnit.setType(this.StringToBeUsed);
		
		classUnit.getCodeElement().add(storableUnit);
		
		System.out.println("A String obtida " +  this.StringToBeUsed.getName());
		
	}
	
	
	private ClassUnit getStringType (CodeModel codeModel) {
		
		
		ClassUnit stringToBeRetorned = null;
		
		EList<AbstractCodeElement> codeElements = codeModel.getCodeElement();
		
		for (AbstractCodeElement abstractCodeElement : codeElements) {
			
			if (abstractCodeElement instanceof Package) {
				
				EList<AbstractCodeElement> packages = ((Package) abstractCodeElement).getCodeElement();
				
				for (AbstractCodeElement abstractCodeElement2 : packages) {
					
					if ( ( abstractCodeElement2 instanceof Package ) && ((Package) abstractCodeElement2).getName().equalsIgnoreCase("lang") ) {
						
						
						EList<AbstractCodeElement> stuffs = ((Package) abstractCodeElement2).getCodeElement();
						
						for (AbstractCodeElement abstractCodeElement3 : stuffs) {
							
							if (( abstractCodeElement3 instanceof ClassUnit ) && ( ((ClassUnit) abstractCodeElement3).getName().equalsIgnoreCase("String") )) {
								
								stringToBeRetorned = (ClassUnit) abstractCodeElement3;
								
							}
							
						}
						
					}
					
					
				}
				
				
				
			}
			
		}
		
		return stringToBeRetorned;
		
	}
	
	private Attribute criarAttibuteForName () {
		
		Attribute att = KdmFactory.eINSTANCE.createAttribute();
		
		att.setTag("export");
		att.setValue("private");
		
		return att;
		
	}
	
	private void generateNewJavaModelWithComment() {
		
		IResource resrouce = ProjectSelectedToModernize.projectSelected.getProject().findMember("/MODELS_PSM_AS_IS/javaModel.javaxmi");
		
		IFile fileToBeRead = (IFile) resrouce;
		
		
		
		UtilJavaModel utilJavaModel = new UtilJavaModel();
		
		Model modelToBeCommented =  utilJavaModel.load(fileToBeRead.getLocationURI().toString());
		
		CreateCommentOnJavaModelBasedInSqlStatement createComment = new CreateCommentOnJavaModelBasedInSqlStatement();
		
		createComment.createCommentOnTheJavaModel(modelToBeCommented);
		
		utilJavaModel.save(modelToBeCommented);
		
		try {
			utilJavaModel.generateNewSourceCode();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
	}
	
	
	private  void setChoseModernization() {
		
		ReestructuringToBeRealized.modernizeToDAO = this.page.getBtnRadioButton().getSelection();
		
		ReestructuringToBeRealized.modernizeToJPA = this.page.getBtnModernizeToJpa().getSelection();
		
		ReestructuringToBeRealized.modernizeToRESTFULL = this.page.getBtnModernizeToRestufull().getSelection();
		
		ReestructuringToBeRealized.modernizeToRESTFULLMobile = this.page.getBtnCheckButton().getSelection();
		
		
	}
	

}
