import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EmploymentCertificateDetailComponent } from './employment-certificate-detail.component';

describe('EmploymentCertificate Management Detail Component', () => {
  let comp: EmploymentCertificateDetailComponent;
  let fixture: ComponentFixture<EmploymentCertificateDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EmploymentCertificateDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ employmentCertificate: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(EmploymentCertificateDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(EmploymentCertificateDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load employmentCertificate on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.employmentCertificate).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
