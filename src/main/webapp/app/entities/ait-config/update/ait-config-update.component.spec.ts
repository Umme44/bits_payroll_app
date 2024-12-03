import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { AitConfigFormService } from './ait-config-form.service';
import { AitConfigService } from '../service/ait-config.service';
import { IAitConfig } from '../ait-config.model';

import { AitConfigUpdateComponent } from './ait-config-update.component';

describe('AitConfig Management Update Component', () => {
  let comp: AitConfigUpdateComponent;
  let fixture: ComponentFixture<AitConfigUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let aitConfigFormService: AitConfigFormService;
  let aitConfigService: AitConfigService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [AitConfigUpdateComponent],
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
      .overrideTemplate(AitConfigUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AitConfigUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    aitConfigFormService = TestBed.inject(AitConfigFormService);
    aitConfigService = TestBed.inject(AitConfigService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const aitConfig: IAitConfig = { id: 456 };

      activatedRoute.data = of({ aitConfig });
      comp.ngOnInit();

      expect(comp.aitConfig).toEqual(aitConfig);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAitConfig>>();
      const aitConfig = { id: 123 };
      jest.spyOn(aitConfigFormService, 'getAitConfig').mockReturnValue(aitConfig);
      jest.spyOn(aitConfigService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ aitConfig });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: aitConfig }));
      saveSubject.complete();

      // THEN
      expect(aitConfigFormService.getAitConfig).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(aitConfigService.update).toHaveBeenCalledWith(expect.objectContaining(aitConfig));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAitConfig>>();
      const aitConfig = { id: 123 };
      jest.spyOn(aitConfigFormService, 'getAitConfig').mockReturnValue({ id: null });
      jest.spyOn(aitConfigService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ aitConfig: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: aitConfig }));
      saveSubject.complete();

      // THEN
      expect(aitConfigFormService.getAitConfig).toHaveBeenCalled();
      expect(aitConfigService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAitConfig>>();
      const aitConfig = { id: 123 };
      jest.spyOn(aitConfigService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ aitConfig });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(aitConfigService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
