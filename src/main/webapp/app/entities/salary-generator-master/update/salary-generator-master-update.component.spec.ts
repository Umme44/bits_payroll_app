import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { SalaryGeneratorMasterFormService } from './salary-generator-master-form.service';
import { SalaryGeneratorMasterService } from '../service/salary-generator-master.service';
import { ISalaryGeneratorMaster } from '../salary-generator-master.model';

import { SalaryGeneratorMasterUpdateComponent } from './salary-generator-master-update.component';

describe('SalaryGeneratorMaster Management Update Component', () => {
  let comp: SalaryGeneratorMasterUpdateComponent;
  let fixture: ComponentFixture<SalaryGeneratorMasterUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let salaryGeneratorMasterFormService: SalaryGeneratorMasterFormService;
  let salaryGeneratorMasterService: SalaryGeneratorMasterService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [SalaryGeneratorMasterUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(SalaryGeneratorMasterUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SalaryGeneratorMasterUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    salaryGeneratorMasterFormService = TestBed.inject(SalaryGeneratorMasterFormService);
    salaryGeneratorMasterService = TestBed.inject(SalaryGeneratorMasterService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const salaryGeneratorMaster: ISalaryGeneratorMaster = { id: 456 };

      activatedRoute.data = of({ salaryGeneratorMaster });
      comp.ngOnInit();

      expect(comp.salaryGeneratorMaster).toEqual(salaryGeneratorMaster);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISalaryGeneratorMaster>>();
      const salaryGeneratorMaster = { id: 123 };
      jest.spyOn(salaryGeneratorMasterFormService, 'getSalaryGeneratorMaster').mockReturnValue(salaryGeneratorMaster);
      jest.spyOn(salaryGeneratorMasterService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ salaryGeneratorMaster });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: salaryGeneratorMaster }));
      saveSubject.complete();

      // THEN
      expect(salaryGeneratorMasterFormService.getSalaryGeneratorMaster).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(salaryGeneratorMasterService.update).toHaveBeenCalledWith(expect.objectContaining(salaryGeneratorMaster));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISalaryGeneratorMaster>>();
      const salaryGeneratorMaster = { id: 123 };
      jest.spyOn(salaryGeneratorMasterFormService, 'getSalaryGeneratorMaster').mockReturnValue({ id: null });
      jest.spyOn(salaryGeneratorMasterService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ salaryGeneratorMaster: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: salaryGeneratorMaster }));
      saveSubject.complete();

      // THEN
      expect(salaryGeneratorMasterFormService.getSalaryGeneratorMaster).toHaveBeenCalled();
      expect(salaryGeneratorMasterService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISalaryGeneratorMaster>>();
      const salaryGeneratorMaster = { id: 123 };
      jest.spyOn(salaryGeneratorMasterService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ salaryGeneratorMaster });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(salaryGeneratorMasterService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
