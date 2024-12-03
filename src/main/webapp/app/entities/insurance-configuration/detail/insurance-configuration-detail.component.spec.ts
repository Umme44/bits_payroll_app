import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { InsuranceConfigurationDetailComponent } from './insurance-configuration-detail.component';

describe('InsuranceConfiguration Management Detail Component', () => {
  let comp: InsuranceConfigurationDetailComponent;
  let fixture: ComponentFixture<InsuranceConfigurationDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [InsuranceConfigurationDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ insuranceConfiguration: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(InsuranceConfigurationDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(InsuranceConfigurationDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load insuranceConfiguration on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.insuranceConfiguration).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
