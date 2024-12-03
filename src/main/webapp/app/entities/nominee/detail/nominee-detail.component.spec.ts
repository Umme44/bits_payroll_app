import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { NomineeDetailComponent } from './nominee-detail.component';

describe('Nominee Management Detail Component', () => {
  let comp: NomineeDetailComponent;
  let fixture: ComponentFixture<NomineeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [NomineeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ nominee: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(NomineeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(NomineeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load nominee on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.nominee).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
