export interface Feature {
    icon: string;
    title: string;
    description: string;
}

export const FEATURES: Feature[] = [
    {
        icon: '🔍',
        title: 'Búsqueda de Guarderías',
        description: 'Encuentra la guardería perfecta según ubicación, horarios, servicios y reseñas de otros padres.'
    },
    {
        icon: '📝',
        title: 'Matrícula Online',
        description: 'Completa el proceso de matrícula sin papeleo. Formularios digitales y pago seguro en línea.'
    },
    {
        icon: '📊',
        title: 'Seguimiento de Desarrollo',
        description: 'Visualiza el progreso de tu hijo con reportes periódicos y evaluaciones de desempeño.'
    },
    {
        icon: '💰',
        title: 'Gestión de Pagos',
        description: 'Paga mensualidades, material didáctico y servicios adicionales en un solo lugar.'
    },
    {
        icon: '📱',
        title: 'Comunicación Directa',
        description: 'Mantente conectado con los educadores a través de mensajes, avisos y calendario de actividades.'
    }
];